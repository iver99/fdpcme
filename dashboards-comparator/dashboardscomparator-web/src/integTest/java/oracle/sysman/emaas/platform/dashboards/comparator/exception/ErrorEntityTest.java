package oracle.sysman.emaas.platform.dashboards.comparator.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ErrorEntityTest {
	
	@Test
	public void testZdtErrorEntity() {
		
		ErrorEntity entity = new ErrorEntity();
		entity.setErrorCode(ZDTErrorConstants.GENERIC_ERROR_CODE);
		entity.setErrorMessage("any exception message");
		
		ErrorEntity entity2 = new ErrorEntity(ZDTErrorConstants.GENERIC_ERROR_CODE,"any exception message" );
		
		Assert.assertEquals(entity.getErrorCode(), entity2.getErrorCode());
		Assert.assertEquals(entity.getErrorMessage(), entity2.getErrorMessage());
	}

}
