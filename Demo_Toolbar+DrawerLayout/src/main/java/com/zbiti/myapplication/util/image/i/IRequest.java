package com.zbiti.myapplication.util.image.i;

/**
 * 图片路径 filePathList或者filePath 的回调接口
 * @param <T>
 */
public interface IRequest<T> {

    void request(T t);

}
