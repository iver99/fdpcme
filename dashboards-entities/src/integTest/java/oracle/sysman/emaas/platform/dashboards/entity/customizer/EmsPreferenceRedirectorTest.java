package oracle.sysman.emaas.platform.dashboards.entity.customizer;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.DeleteObjectQuery;
import org.eclipse.persistence.queries.InsertObjectQuery;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;
import org.testng.annotations.Test;

/**
 * Created by xiadai on 2017/1/11.
 */
@Test(groups = {"s2"})
public class EmsPreferenceRedirectorTest {
    private EmsPreferenceRedirector emsPreferenceRedirector = new EmsPreferenceRedirector();

    @Mocked
    AbstractRecord record;
    @Mocked
    AbstractSession session;
    @Mocked
    ClassDescriptor classDescriptor;
    @Mocked
    EmsPreference emsPreference;
    @Mocked
    UpdateObjectQuery updateObjectQuery;
    @Test
    public void testInvokeQuery(@Mocked final DeleteObjectQuery deleteQuery) throws Exception {
        new Expectations(){
            {
                deleteQuery.isDeleteObjectQuery();
                result = true;
                deleteQuery.getReferenceClass();
                result = this.getClass();
                session.getDescriptor(this.getClass());
                result = classDescriptor;
                session.getActiveSession();
                result = session;
                session.getProperty(anyString);
                result = 1;
                deleteQuery.getObject();
                result = emsPreference;
                updateObjectQuery.execute((AbstractSession)any, (AbstractRecord)any);
            }
        };
        emsPreferenceRedirector.invokeQuery(deleteQuery, record, session);
    }

    @Test
    public void testInvokeQuery(@Mocked final InsertObjectQuery insertQuery) throws Exception {
        new Expectations(){
            {
                insertQuery.isDeleteObjectQuery();
                result = false;
                insertQuery.isInsertObjectQuery();
                result = true;
                insertQuery.getReferenceClass();
                result = this.getClass();
                session.getDescriptor(this.getClass());
                result = classDescriptor;
                session.getActiveSession();
                result = session;
                session.getProperty(anyString);
                result = 1;
                insertQuery.getObject();
                result = emsPreference;
                updateObjectQuery.execute((AbstractSession)any, (AbstractRecord)any);
            }
        };
        emsPreferenceRedirector.invokeQuery(insertQuery, record, session);
    }

    @Test
    public void testInvokeQuery(@Mocked final DatabaseQuery databaseQuery) throws Exception {
        new Expectations(){
            {
                databaseQuery.isDeleteObjectQuery();
                result = false;
                databaseQuery.isInsertObjectQuery();
                result = false;
                databaseQuery.getReferenceClass();
                result = this.getClass();
                session.getDescriptor(this.getClass());
                result = classDescriptor;
                session.getActiveSession();
                result = session;
                session.getProperty(anyString);
                result = 1;
                databaseQuery.execute((AbstractSession)any, (AbstractRecord)any);
            }
        };
        emsPreferenceRedirector.invokeQuery(databaseQuery, record, session);
    }

}