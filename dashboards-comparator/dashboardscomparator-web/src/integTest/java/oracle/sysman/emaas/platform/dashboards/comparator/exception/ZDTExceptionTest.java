package oracle.sysman.emaas.platform.dashboards.comparator.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ZDTExceptionTest {
	
	@Test
	public void testZDTException() {
		ZDTException e1 = new ZDTException(ZDTErrorConstants.NULL_RETRIEVED_ERROR_CODE,ZDTErrorConstants.NULL_RETRIEVED_ERROR_MESSAGE);
		ZDTException e2 = new ZDTException(ZDTErrorConstants.NULL_LINK_ERROR_CODE,ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE);
		ZDTException e3 = new ZDTException(ZDTErrorConstants.FAIL_TO_SYNC_ERROR_CODE,ZDTErrorConstants.FAIL_TO_SYNC_ERROR_MESSAGE);
		ZDTException e4 = new ZDTException(ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_CODE,ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_MESSAGE);
		e4.setErrorCode(ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_CODE);
		e4.setErrorMessage(ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_MESSAGE);
			
		Assert.assertNotNull(e1.getErrorMessage());
		Assert.assertNotEquals(e1.getErrorCode(), e2.getErrorCode());
	}

}
