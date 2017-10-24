package com.zmsoft.TestTool.utils.okhttp.listener;

/**
 * @author vision
 * @function 监听下载进度
 */
public interface DisposeDownloadListener extends DisposeDataListener {
	void onProgress(int progrss);
}
