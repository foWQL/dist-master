package com.pansky.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Fo
 * @date 2023/4/27 14:41
 */


@Component
@Slf4j
public class EsBaseSearchUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 判断索引是否存在
     *
     * @param indexName
     * @return
     */
    public  boolean isIndexExist(String indexName) throws Exception {
        IndicesClient indicesClient = restHighLevelClient.indices();
        // 创建get请求
        GetIndexRequest request = new GetIndexRequest(indexName);
        // 判断索引库是否存在
        boolean result = indicesClient.exists(request, RequestOptions.DEFAULT);

        return result;
    }


    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public boolean createIndex(String indexName) throws Exception {

        // 1、创建 创建索引request 参数：索引名mess
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        // 2、设置索引的settings
        // 3、设置索引的mappings
        String mapping = "{\n" +
                "\n" +
                "\t\t\"properties\": {\n" +
                "\t\t  \"brandName\": {\n" +
                "\t\t\t\"type\": \"keyword\"\n" +
                "\t\t  },\n" +
                "\t\t  \"categoryName\": {\n" +
                "\t\t\t\"type\": \"keyword\"\n" +
                "\t\t  },\n" +
                "\t\t  \"createTime\": {\n" +
                "\t\t\t\"type\": \"date\",\n" +
                "\t\t\t\"format\": \"yyyy-MM-dd HH:mm:ss\"\n" +
                "\t\t  },\n" +
                "\t\t  \"id\": {\n" +
                "\t\t\t\"type\": \"long\"\n" +
                "\t\t  },\n" +
                "\t\t  \"price\": {\n" +
                "\t\t\t\"type\": \"double\"\n" +
                "\t\t  },\n" +
                "\t\t  \"saleNum\": {\n" +
                "\t\t\t\"type\": \"integer\"\n" +
                "\t\t  },\n" +
                "\t\t  \"status\": {\n" +
                "\t\t\t\"type\": \"integer\"\n" +
                "\t\t  },\n" +
                "\t\t  \"stock\": {\n" +
                "\t\t\t\"type\": \"integer\"\n" +
                "\t\t  },\n" +
                "\t\t\"spec\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
                "\t\t  },\n" +
                "\t\t  \"title\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
                "\t\t  }\n" +
                "\t\t}\n" +
                "  }";
        // 4、 设置索引的别名
        // 5、 发送请求
        // 5.1 同步方式发送请求
        IndicesClient indicesClient = restHighLevelClient.indices();
        indexRequest.mapping(mapping, XContentType.JSON);
        // 请求服务器
        CreateIndexResponse response = indicesClient.create(indexRequest, RequestOptions.DEFAULT);
        log.info("执行建立成功：{}" , response.isAcknowledged());
        return response.isAcknowledged();
    }


    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    public   boolean deleteIndex(String indexName) throws Exception {
        if(!isIndexExist(indexName)) {
            log.info("Index is not exits!");
        }
        IndicesClient indicesClient = restHighLevelClient.indices();
        // 创建delete请求方式
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        // 发送delete请求
        AcknowledgedResponse response = indicesClient.delete(deleteIndexRequest, RequestOptions.DEFAULT);

        return response.isAcknowledged();
    }

    /**
     * 获取index的Mapping
     * GET goods/_mapping
     */
    public Map<String, Object> getMapping(String indexName) throws Exception {
        IndicesClient indicesClient = restHighLevelClient.indices();

        // 创建get请求
        GetIndexRequest request = new GetIndexRequest(indexName);
        // 发送get请求
        GetIndexResponse response = indicesClient.get(request, RequestOptions.DEFAULT);
        // 获取表结构
        Map<String, MappingMetadata> mappings = response.getMappings();
        Map<String, Object> sourceAsMap = mappings.get(indexName).getSourceAsMap();
        return sourceAsMap;
    }

    /**
     * 增加文档信息
     */
    public RestStatus addDocument(String indexName, String type, JSONObject jsonObject) throws IOException {
        // 默认类型为_doc
        type = StringUtils.isEmpty(type) ? "_doc" : type;
        // 将对象转为json
        String data = JSON.toJSONString(jsonObject);
        // 创建索引请求对象
        IndexRequest indexRequest = new IndexRequest(indexName,type)
             //   .id(jsonObject.getId())    如果需要指定对象的id作为索引中数据id
                .source(data, XContentType.JSON);
        // 执行增加文档
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);


        // indexRequest.create(false);        //数据为存储而不是更新
        // 异步插入
        /*restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        log.error("将id为：{}的数据存入ES时存在失败的分片，原因为：{}", indexRequest.id(), failure.getCause());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                log.error("{}:存储es时异常，数据信息为", indexRequest.id(), e);
            }
        });*/
        RestStatus status = response.status();
        log.info("创建状态：{}", status);

        return status;
    }

    /**
     * 批量插入文档信息
     */
    public <T> RestStatus batchImportGoodsData(String indexName, String type, List<T>  list) throws IOException {

        // 默认类型为_doc
        type = StringUtils.isEmpty(type) ? "_doc" : type;

        //2.bulk导入
        BulkRequest bulkRequest = new BulkRequest();

        //2.1 循环goodsList，创建IndexRequest添加数据
        for (T t : list) {

            //将goods对象转换为json字符串
            String data = JSON.toJSONString(t);//map --> {}
            IndexRequest indexRequest = new IndexRequest(indexName,type);
            indexRequest
                 //   .id(t.getId() + "")   如果需要指定对象的id作为索引中数据id
                    .source(data, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        restHighLevelClient.bulkAsync()   异步批量
        return response.status();
    }

    /**
     * 通过ID删除数据
     *
     * @param indexName 索引
     * @param type  类型
     * @param id    数据ID
     */
    public RestStatus deleteDataById(String indexName, String type, String id) throws IOException {
        // 默认类型为_doc
        type = StringUtils.isEmpty(type) ? "_doc" : type;
        // 创建删除请求对象
        DeleteRequest deleteRequest = new DeleteRequest(indexName, type, id);
        // 执行删除文档
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

        RestStatus status = response.status();

        log.info("删除文档响应状态：{}", status);
        return status;
    }

    /**
     * 更新文档信息
     */
    public RestStatus updateDocument(String indexName, String type, JSONObject jsonObject) throws IOException {
        // 默认类型为_doc
        type = StringUtils.isEmpty(type) ? "_doc" : type;

        // 将对象转为json
        String jsonStr = JSON.toJSONString(jsonObject);
        // 创建索引请求对象
        UpdateRequest updateRequest = new UpdateRequest(indexName, type);
        // 设置更新文档内容
        updateRequest.doc(jsonStr, XContentType.JSON);
        // 执行更新文档
        UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

        RestStatus status = response.status();

        log.info("更新文档信息响应状态：{}", status);

        return status;
    }

    /**
     *  按条件更新文档信息
     */
    public BulkByScrollTask.Status updateDocByCond(String indexName, String type) throws IOException {
        // 默认类型为_doc
        type = StringUtils.isEmpty(type) ? "_doc" : type;

        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.setQuery(new TermQueryBuilder("id", 3001));
        updateByQueryRequest.setScript(new Script(ScriptType.INLINE,
                "painless",
                "ctx._source.tag='电脑'", Collections.emptyMap()));

        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);

        BulkByScrollTask.Status status = bulkByScrollResponse.getStatus();

        log.info("更新文档信息响应状态：{}", status);

        return status;
    }

    /**
     * 获取文档信息
     */
    public Map<String,Object> searchDataById(String indexName, String type, String id) throws Exception {
        // 默认类型为_doc
        type = StringUtils.isEmpty(type) ? "_doc" : type;

        // 创建获取请求对象
        GetRequest getRequest = new GetRequest(indexName, type, id);
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> sourceAsMap = response.getSourceAsMap();

        return sourceAsMap;

    }




}
