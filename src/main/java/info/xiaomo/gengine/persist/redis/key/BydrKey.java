package info.xiaomo.gengine.persist.redis.key;

/**
 * 捕鱼达人redis数据key枚举
 *
 *
 *
 */
public enum BydrKey {

	/** 角色基本信息 */
	Team_Map("Bydr:Team:Map"),
	/**角色信息*/
	Role_Map("Bydr:Role_%d:Map")
	;

	private final String key;

	BydrKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
}
