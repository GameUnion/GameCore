package info.xiaomo.gengine.common.handler;

import info.xiaomo.gengine.common.bean.Config;
import info.xiaomo.gengine.common.utils.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取线程信息
 *
 * <p>http://192.168.0.17:9002/server/thread/info?auth=daa0cf5b-e72d-422c-b278-450b28a702c6
 * <p>
 * 2017年10月12日 下午2:38:44
 */
@HandlerEntity(path = "/server/thread/info")
public class ThreadInfoHandler extends HttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadInfoHandler.class);

	@Override
	public void run() {
		String auth = getString("auth");
		if (!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		String info = SysUtil.threadInfo("<br>");
		LOGGER.info(info);
		info = info.trim().replaceAll("/n", "").replaceAll("/t", "&nbsp;&nbsp;");

		sendMsg(info);
	}
}
