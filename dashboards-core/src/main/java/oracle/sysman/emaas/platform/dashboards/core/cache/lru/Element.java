package oracle.sysman.emaas.platform.dashboards.core.cache.lru;

import java.io.Serializable;

@SuppressWarnings("all")
public class Element implements Serializable{
	private  Object key;
    private  Object value;
    private transient long creationTime;
	private transient long lastAccessTime;
    
    public Element(Object key, Object value){
    	this.key=key;
    	this.value=value;
    	this.creationTime=getCurrentTime();
    	this.lastAccessTime=creationTime;
    }

	/**
	 * currently,we only compare the key,not the value
	 */
    public final boolean equals(Object object)
    {
        if(object == null || !(object instanceof Element))
            return false;
        Element element = (Element)object;
        if(key == null || element.getKey() == null)
            return false;
        else
            return key.equals(element.getKey());
    }
    
    
    public final int hashCode()
    {
        return key.hashCode();
    }
    
    public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	private long getCurrentTime()
    {
        return System.currentTimeMillis();
    }
    
    public final String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.
        append("[ key = ").append(key).
        append(", value=").append(value).
        append(" ]");
        return sb.toString();
    }

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	/**
	 * check if the element is exipired.
	 * @param timeToLive
	 * @return true if expired,false if not. 
	 */
	public boolean isExpired(int timeToLive){
		if(timeToLive <=0){
			return false;//eternal cache
		}
		return getCurrentTime()-lastAccessTime>TimeUtil.toMillis(timeToLive);
	}
    
    
}

