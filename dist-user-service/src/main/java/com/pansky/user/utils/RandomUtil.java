package com.pansky.user.utils;

/**
 * @author Fo
 * @date 2022/12/11 16:17
 */
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class RandomUtil {

    /**
     * 生成指定范围的随机整数
     *
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static int genInteger(Integer min, Integer max) {
        return (int) (Math.random() * (max - min + 1) + min); // ctrl+1
    }

    /**
     * 生成长度随机的邮箱
     *
     * @return
     */
    public static String genEmail() {
        String[] emails = { "126", "163", "qq", "gmail", "hotmail", "outlook" };
        String[] suffix = { "com", "cn", "net", "org" };
        String tmp1 = genString(5, genInteger(6, 18));
        String tmp2 = emails[genInteger(0, emails.length - 1)];
        String tmp3 = suffix[genInteger(0, suffix.length - 1)];
        return tmp1 + "@" + tmp2 + "." + tmp3;
    }

    /**
     * 随机生成电话号码
     *
     * @return
     */
    public static String genPhoneNum() {
        String[] prefix = { "132", "133", "134", "135", "136", "138", "139", "152", "154", "155", "177", "188" };
        int index = genInteger(0, prefix.length - 1);
        String res = prefix[index] + genString(1, 8);
        return res;
    }

    /**
     * 随机产生一个boolean值
     *
     * @return 返回true或false
     */
    public static boolean genBoolean() {
        boolean res = genInteger(0, 1) == 0 ? true : false;
        return res;
    }

    /**
     * 随机字符串：1纯数字，2纯小写，3纯大写，4大小写，5数字字母混合，6汉字字符串
     *
     * @param type 类型
     * @param len  长度
     * @return
     */
    public static String genString(Integer type, Integer len) {
        String res = "";
        switch (type) {
            case 1:
                for (int i = 0; i < len; i++) {
                    res += genInteger(0, 9);
                }
                break;
            case 2:
                for (int i = 0; i < len; i++) {
                    int min = (int) 'a';
                    int max = (int) 'z';
                    int tmp = genInteger(min, max);
                    res += (char) tmp;
                }
                break;
            case 3:
                for (int i = 0; i < len; i++) {
                    int min = (int) 'A';
                    int max = (int) 'Z';
                    int tmp = genInteger(min, max);
                    res += (char) tmp;
                }
                break;
            case 4:
                for (int i = 0; i < len; i++) {
                    int min = (int) 'A';
                    int max = (int) 'z';
                    int tmp = genInteger(min, max);
                    if (tmp >= 91 && tmp <= 96) {
                        i--;
                        continue;
                    }
                    res += (char) tmp;
                }
                break;
            case 5:
                for (int i = 0; i < len; i++) {
                    int min = (int) '0';
                    int max = (int) 'z';
                    int tmp = genInteger(min, max);
                    if ((tmp >= 91 && tmp <= 96) || (tmp >= 58 && tmp <= 64)) {
                        i--;
                        continue;
                    }
                    res += (char) tmp;
                }
                break;
            case 6:
                for (int i = 0; i < len; i++) {
                    res += genRandomChineseChar();
                }
                break;

            default:
                throw new RuntimeException("类型不正确");
        }
        return res;
    }

    /**
     * 随机生成一个常见的汉字字符
     *
     * @return
     */
    private static char genRandomChineseChar() {
        String str = "";
        Random random = new Random();

        int hightPos = (176 + Math.abs(random.nextInt(39)));
        int lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }
        return str.charAt(0);
    }

    /**
     * 随机生成中文名字
     *
     * @return
     */
    public static String genChineseName() {
        Random random = new Random();
        String firstNames = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季";
        String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
        String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";

        int index = random.nextInt(firstNames.length() - 1);
        String name = "" + firstNames.charAt(index); // 获得一个随机的姓氏

        int i = random.nextInt(3);// 可以根据这个数设置产生的男女比例
        if (i == 2) {
            int j = random.nextInt(girl.length() - 2);
            if (j % 2 == 0) {
                name = name + girl.substring(j, j + 2);
            } else {
                name = name + girl.substring(j, j + 1);
            }
        } else {
            int j = random.nextInt(girl.length() - 2);
            if (j % 2 == 0) {
                name = name + boy.substring(j, j + 2);
            } else {
                name = name + boy.substring(j, j + 1);
            }

        }
        return name;
    }

    /**
     * 生成指定范围内的随机日期时间
     *
     * @param min 起始日期，比如：2000-3-3 8:28:58
     * @param max 终止日期，比如：2004-12-12 12:12:12
     * @return
     */
    public static LocalDateTime genLocalDateTime(String min, String max) {
        if (!(min.contains(" ") || min.contains("-") || min.contains(":"))) {
            throw new RuntimeException("日期参数格式不对");
        }
        if (!(max.contains(" ") || max.contains("-") || max.contains(":"))) {
            throw new RuntimeException("日期参数格式不对");
        }
        String[] mins = min.split("[ ,\\-,:]");
        String[] maxs = max.split("[ ,\\-,:]");

        LocalDateTime localDateTimeMin = LocalDateTime.of(Integer.parseInt(mins[0]), Integer.parseInt(mins[1]),
                Integer.parseInt(mins[2]), Integer.parseInt(mins[3]), Integer.parseInt(mins[4]),
                Integer.parseInt(mins[5]));
        LocalDateTime localDateTimeMax = LocalDateTime.of(Integer.parseInt(maxs[0]), Integer.parseInt(maxs[1]),
                Integer.parseInt(maxs[2]), Integer.parseInt(mins[3]), Integer.parseInt(mins[4]),
                Integer.parseInt(mins[5]));

        // 获取日期所对应的数字
        long timeMin = localDateTimeMin.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long timeMax = localDateTimeMax.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        double random = Math.random(); // [0,1)
        long digit = (long) (random * (timeMax - timeMin + 1) + timeMin);

        LocalDateTime res = LocalDateTime.ofInstant(Instant.ofEpochMilli(digit), ZoneOffset.of("+8"));

        return res;
    }

    /**
     * 生成指定范围内的随机日期
     *
     * @param min 起始日期，比如：2000-3-3
     * @param max 终止日期，比如：2004-12-12
     * @return
     */
    public static LocalDate genLocalDate(String min, String max) {
        if (!min.contains("-")) {
            throw new RuntimeException("日期参数格式不对");
        }
        if (!max.contains("-")) {
            throw new RuntimeException("日期参数格式不对");
        }
        min += " 0:0:0";
        max += " 23:59:60";
        LocalDateTime res = genLocalDateTime(min, max);

        return res.toLocalDate();
    }

    /**
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        b[0] = (ip >> 24) & 0xff;
        b[1] = (ip >> 16) & 0xff;
        b[2] = (ip >> 8) & 0xff;
        b[3] = ip & 0xff;
        // 拼接 IP
        String x = b[0] + "." + b[1] + "." + b[2] + "." + b[3];
        return x;
    }

/**
 * 随机生成IP
 **/
    public static String genRandomIp() {
        // 指定 IP 范围
        int[][] range = {
                {607649792, 608174079}, // 36.56.0.0-36.63.255.255
                {1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
                {1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
                {2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
                {2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
                {-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
                {-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
                {-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
                {-770113536, -768606209}, // 210.25.0.0-210.47.255.255
                {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
        };

        Random random = new Random();
        int index = random.nextInt(10);
        String ip = num2ip(range[index][0] + random.nextInt(range[index][1] - range[index][0]));
        return ip;
    }


    public static void main(String[] args) {
        System.out.println(genChineseName());
        System.out.println(genInteger(10, 99));
        System.out.println(genPhoneNum());
        System.out.println(genEmail());
        System.out.println(genBoolean());
        System.out.println(genRandomIp());
        System.out.println(genString(1, 8));
        System.out.println(genString(2, 8));
        System.out.println(genString(3, 8));
        System.out.println(genString(4, 8));
        System.out.println(genString(5, 8));
        System.out.println(genString(6, 8));

        for (int i = 0; i < 3; i++) {
            LocalDateTime res = RandomUtil.genLocalDateTime("1999-12-12 8:28:58", "2000-12-12 11:3:22");
            System.out.println(res);
            System.out.println(genLocalDate("1999-9-21", "2019-9-21"));
        }
    }

}
