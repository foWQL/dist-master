package com.pansky.es.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.pansky.es.bean.EsPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fo
 * @date 2023/4/27 18:40
 */

@Component
@Slf4j
public class EsDslSearchUitl {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    //*********************** DSL高级查询操作 *********************//

    /**
     * 精确查询（termQuery）
     */
    public <T> List<T> termQuery(String indexName, String field, Object value, Class<T> beanClass) {
        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(field, value);
            searchSourceBuilder.query(termsQueryBuilder);
            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("精确查询数据失败，错误信息：" + e.getStackTrace());
        }
        return list;
    }


    /**
     * terms:多个查询内容在一个字段中进行查询
     */
    public <T> List<T> termsQuery(String indexName, String field, Object[] dataArgs, Class<T> beanClass) {

        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件（注意：termsQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(field, dataArgs);
            searchSourceBuilder.query(termsQueryBuilder);
            // 展示100条,默认只展示10条记录
            searchSourceBuilder.size(100);
            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("单字段多内容查询数据失败，错误信息：" + e.getStackTrace());
        }
        return list;
    }

    /**
     * 匹配查询符合条件的所有数据，并设置分页
     */
    public <T> List<T>  matchAllQuery(String indexName, Class<T> beanClass, int startIndex, int pageSize, List<String> orderList, String field, Object value) {

        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 构建查询条件
            if (!ObjectUtil.isEmpty(field) && !ObjectUtil.isEmpty(value)) {
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field, value);
                searchSourceBuilder.query(matchQueryBuilder);
            } else {
                MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
                searchSourceBuilder.query(matchAllQueryBuilder);
            }

            // 设置分页
            searchSourceBuilder.from(startIndex);
            searchSourceBuilder.size(pageSize);

            // 设置排序
            if (orderList != null) {
                for(String order : orderList) {
                    // -开头代表：倒序
                    boolean flag = order.startsWith("-");
                    SortOrder sort = flag ? SortOrder.DESC: SortOrder.ASC;
                    order = flag ? order.substring(1) : order;

                    searchSourceBuilder.sort(order, sort);
                }
            }

            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("查询所有数据失败，错误信息：" + e.getStackTrace());
        }

        return list;
    }

    /**
     * 词语匹配查询
     */
    public <T> List<T> matchPhraseQuery(String indexName, Class<T> beanClass, String field, Object value) {

        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(field, value);
            searchSourceBuilder.query(matchPhraseQueryBuilder);

            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("词语匹配查询失败，错误信息：" + e.getStackTrace());
        }

        return list;
    }

    /**
     * 内容在多字段中进行查询
     */
    public <T> List<T> matchMultiQuery(String indexName, Class<T> beanClass, String[] fields, Object text) {

        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 设置查询条件
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(text, fields);
            searchSourceBuilder.query(multiMatchQueryBuilder);

            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("词语匹配查询失败，错误信息：" + e.getStackTrace());
        }

        return list;
    }

    /**
     * 通配符查询(wildcard)：会对查询条件进行分词。还可以使用通配符 ?（任意单个字符） 和 * （0个或多个字符）
     *
     * *：表示多个字符（0个或多个字符）
     * ?：表示单个字符
     */
    public <T> List<T> wildcardQuery(String indexName, Class<T> beanClass,String field, String text) {
        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(field, text);
            searchSourceBuilder.query(wildcardQueryBuilder);
            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("通配符查询失败，错误信息：" + e.getStackTrace());
        }
        return list;
    }

    /**
     * 模糊查询: 所有以 “三” 结尾的商品信息
     */
    public <T> List<T> fuzzyQuery(String indexName, Class<T> beanClass, String field, String text) {
        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery(field, text).fuzziness(Fuzziness.AUTO);
            searchSourceBuilder.query(fuzzyQueryBuilder);
            // 执行查询es数据
            queryEsData(indexName, beanClass, list, searchSourceBuilder);

        } catch (IOException e) {
            log.error("通配符查询失败，错误信息：" + e.getStackTrace());
        }

        return list;
    }

    /**
     * boolQuery 查询
     *      高亮展示标题搜索字段
     *      设置出参返回字段
     *
     * 案例：查询从2018-2022年间标题含 三星 的商品信息
     */
    public  <T> List<T> boolQuery(String indexName, Class<T> beanClass) {
        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 创建 Bool 查询构建器
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            // 构建查询条件
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", "三星")); // 标题
            boolQueryBuilder.must(QueryBuilders.matchQuery("spec", "联通3G"));// 说明书
            boolQueryBuilder.filter().add(QueryBuilders.rangeQuery("createTime").format("yyyy").gte("2018").lte("2022")); // 创建时间

            // 构建查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.size(100);

            // 甚至返回字段
            // 如果查询的属性很少，那就使用includes，而excludes设置为空数组
            // 如果排序的属性很少，那就使用excludes，而includes设置为空数组
            String[] includes = {"title", "categoryName", "price"};
            String[] excludes = {};
            searchSourceBuilder.fetchSource(includes, excludes);

            // 高亮设置
            // 设置高亮三要素:  field: 你的高亮字段 , preTags ：前缀    , postTags：后缀
            HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").preTags("<font color='red'>").postTags("</font>");
            highlightBuilder.field("spec").preTags("<font color='red'>").postTags("</font>");
            searchSourceBuilder.highlighter(highlightBuilder);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    T bean = JSON.parseObject(hit.getSourceAsString(), beanClass);

                    // 获取高亮的数据
                    HighlightField highlightField = hit.getHighlightFields().get("title");
                    System.out.println("高亮名称：" + highlightField.getFragments()[0].string());

                    // 替换掉原来的数据
                    Text[] fragments = highlightField.getFragments();
                    if (fragments != null && fragments.length > 0) {
                        StringBuilder title = new StringBuilder();
                        for (Text fragment : fragments) {
                            title.append(fragment);
                        }
                        // 获取method对象，其中包含方法名称和参数列表
                        Method setTitle = beanClass.getMethod("setTitle", String.class);
                        if (setTitle != null) {
                            // 执行method，bean为实例对象，后面是方法参数列表；setTitle没有返回值
                            setTitle.invoke(bean, title.toString());
                        }
                    }

                    list.add(bean);
                }
            }

        } catch (Exception e) {
            log.error("布尔查询失败，错误信息：" + e.getStackTrace());
        }

        return list;
    }

    /**
     * 聚合查询 : 聚合查询一定是【先查出结果】，然后对【结果使用聚合函数】做处理.
     *
     * Metric 指标聚合分析。常用的操作有：avg：求平均、max：最大值、min：最小值、sum：求和等
     *
     * 案例：分别获取最贵的商品和获取最便宜的商品
     */
    public void aggQuery(String indexName) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            // 获取最贵的商品
            AggregationBuilder maxPrice = AggregationBuilders.max("maxPrice").field("price");
            searchSourceBuilder.aggregation(maxPrice);
            // 获取最便宜的商品
            AggregationBuilder minPrice = AggregationBuilders.min("minPrice").field("price");
            searchSourceBuilder.aggregation(minPrice);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedMax max = aggregations.get("maxPrice");
            log.info("最贵的价格：" + max.getValue());
            ParsedMin min = aggregations.get("minPrice");
            log.info("最便宜的价格：" + min.getValue());

        } catch (Exception e) {
            log.error("指标聚合分析查询失败，错误信息：" + e.getStackTrace());
        }
    }


    /**
     * 分桶聚合查询： 聚合查询一定是【先查出结果】，然后对【结果使用聚合函数】做处理.
     *
     *  Bucket 分桶聚合分析 : 对查询出的数据进行分组group by，再在组上进行游标聚合
     *
     * 案例：根据品牌进行聚合查询
     */
    public void bucketQuery(String indexName,String bucketField, String bucketFieldAlias) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            // 根据bucketField进行分组查询
            TermsAggregationBuilder aggBrandName = AggregationBuilders.terms(bucketFieldAlias).field(bucketField);
            searchSourceBuilder.aggregation(aggBrandName);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms aggBrandName1 = aggregations.get(bucketField); // 分组结果数据
            for (Terms.Bucket bucket : aggBrandName1.getBuckets()) {
                log.info(bucket.getKeyAsString() + "====" + bucket.getDocCount());
            }
        } catch (IOException e) {
            log.error("分桶聚合分析查询失败，错误信息：" + e.getStackTrace());
        }
    }

    /**
     * 子聚合聚合查询
     * Bucket 分桶聚合分析
     *
     *  案例：根据商品分类进行分组查询,并且获取分类商品中的平均价格
     */
    public void subBucketQuery(String indexName,String bucketField, String bucketFieldAlias,String avgFiled,String avgFiledAlias) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            // 根据 bucketField进行分组查询,并且获取分类信息中 指定字段的平均值
            TermsAggregationBuilder subAggregation = AggregationBuilders.terms(bucketFieldAlias).field(bucketField)
                    .subAggregation(AggregationBuilders.avg(avgFiledAlias).field(avgFiled));
            searchSourceBuilder.aggregation(subAggregation);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms aggBrandName1 = aggregations.get(bucketFieldAlias);
            for (Terms.Bucket bucket : aggBrandName1.getBuckets()) {
                // 获取聚合后的 组内字段平均值,注意返回值不是Aggregation对象,而是指定的ParsedAvg对象
                ParsedAvg avgPrice = bucket.getAggregations().get(avgFiledAlias);

                log.info(bucket.getKeyAsString() + "====" + avgPrice.getValueAsString());
            }
        } catch (IOException e) {
            log.error("分桶聚合分析查询失败，错误信息：" + e.getStackTrace());
        }
    }

    /**
     * 综合聚合查询
     *
     * 根据商品分类聚合，获取每个商品类的平均价格，并且在商品分类聚合之上子聚合每个品牌的平均价格
     */
    public void subSubAgg(String indexName) {

        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            // 注意这里聚合写的位置不要写错,很容易搞混,错一个括号就不对了
            TermsAggregationBuilder subAggregation = AggregationBuilders.terms("categoryNameAgg").field("categoryName")
                    .subAggregation(AggregationBuilders.avg("categoryNameAvgPrice").field("price"))
                    .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName")
                            .subAggregation(AggregationBuilders.avg("brandNameAvgPrice").field("price")));
            searchSourceBuilder.aggregation(subAggregation);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //获取总记录数
            log.info("totalHits = " + searchResponse.getHits().getTotalHits());
            // 获取聚合信息
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms categoryNameAgg = aggregations.get("categoryNameAgg");

            //获取值返回
            for (Terms.Bucket bucket : categoryNameAgg.getBuckets()) {
                // 获取聚合后的分类名称
                String categoryName = bucket.getKeyAsString();
                // 获取聚合命中的文档数量
                long docCount = bucket.getDocCount();
                // 获取聚合后的分类的平均价格,注意返回值不是Aggregation对象,而是指定的ParsedAvg对象
                ParsedAvg avgPrice = bucket.getAggregations().get("categoryNameAvgPrice");

                System.out.println(categoryName + "======平均价:" + avgPrice.getValue() + "======数量:" + docCount);

                ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
                for (Terms.Bucket brandeNameAggBucket : brandNameAgg.getBuckets()) {
                    // 获取聚合后的品牌名称
                    String brandName = brandeNameAggBucket.getKeyAsString();

                    // 获取聚合后的品牌的平均价格,注意返回值不是Aggregation对象,而是指定的ParsedAvg对象
                    ParsedAvg brandNameAvgPrice = brandeNameAggBucket.getAggregations().get("brandNameAvgPrice");

                    log.info("     " + brandName + "======" + brandNameAvgPrice.getValue());
                }
            }

        } catch (IOException e) {
            log.error("综合聚合查询失败，错误信息：" + e.getStackTrace());
        }
    }

    /**
     * 使用分词查询,并分页
     *
     * @param indexName          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startPage    当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public EsPage searchDataPage(String indexName, String type, int startPage, int pageSize, QueryBuilder query, String fields, String sortField, String highlightField) throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchSourceBuilder.fetchSource(fields.split(","), null);
        }

        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            searchSourceBuilder.sort(sortField, SortOrder.DESC);
        }

        // 高亮（xxx=111,aaa=222）
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        searchSourceBuilder.query(query);
        //searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());

        // 分页应用
        searchSourceBuilder.from(startPage);
        searchSourceBuilder.size(pageSize);

        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        log.info("\n{}", searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        searchRequest.source(searchSourceBuilder);
        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        long totalHits =  searchResponse.getHits().getTotalHits().value;
        long length = searchResponse.getHits().getHits().length;

        log.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);

            return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
        }

        return null;

    }

    /**
     * 使用分词查询
     *
     * @param indexName       索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param query          查询条件
     * @param dataSize           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public  List<Map<String, Object>> searchListData(String indexName, String type, QueryBuilder query, Integer dataSize, String fields, String sortField, String highlightField) throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        searchSourceBuilder.query(query);

        if (StringUtils.isNotEmpty(fields)) {
            searchSourceBuilder.fetchSource(fields.split(","), null);
        }
        searchSourceBuilder.fetchSource(true);

        if (StringUtils.isNotEmpty(sortField)) {
            searchSourceBuilder.sort(sortField, SortOrder.DESC);
        }

        if (dataSize != null && dataSize > 0) {
            searchSourceBuilder.size(dataSize);
        }

        // 打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        log.info("\n{}", searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        long  totalHits = searchResponse.getHits().getTotalHits().value;
        long length = searchResponse.getHits().getHits().length;

        log.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }
        return null;
    }
    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());

            if (StringUtils.isNotEmpty(highlightField)) {

                System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();

                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }


    /**
     * 执行es查询
     * @param indexName
     * @param beanClass
     * @param list
     * @param searchSourceBuilder
     * @param <T>
     * @throws IOException
     */
    private <T> List<T> queryEsData(String indexName, Class<T> beanClass, List<T> list, SearchSourceBuilder searchSourceBuilder) throws IOException {
        // 创建查询请求对象，将查询对象配置到其中
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 根据状态和数据条数验证是否返回了数据
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                // 将 JSON 转换成对象
//                Goods userInfo = JSON.parseObject(hit.getSourceAsString(), Goods.class);
                // 将 JSON 转换成对象
                T bean = JSON.parseObject(hit.getSourceAsString(), beanClass);
                list.add(bean);
            }
        }
        return list;
    }


}
