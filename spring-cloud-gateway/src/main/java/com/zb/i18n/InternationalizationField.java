package com.zb.i18n;

/**
 * 国际化消息父接口，各个子模块建立自己的枚举类实现该接口
 *
 * @author zhangbo
 * @date 2019-08-22
 */
public interface InternationalizationField {

    /**
     * code，i18n配置的key
     *
     * @return
     */
    String getCode();

    /**
     * 默认消息
     *
     * @return
     */
    String getDefaultMsg();

}
