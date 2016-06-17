package oracle.sysman.emaas.platform.dashboards.core.cache.lru;

public class Binary {
	private byte[] data;
	
	public Binary(byte[] data)  {
		this.data =data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	
	
}
