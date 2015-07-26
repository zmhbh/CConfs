package cmu.cconfs.utils.data;

import android.support.v4.util.Pair;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import cmu.cconfs.model.parseModel.Session_Timeslot;
import cmu.cconfs.model.parseModel.Timeslot;

/**
 * Created by zmhbh on 8/25/15.
 */
public class DataProvider {
    private final int days = 6;

    private List<UnityDataProvider> programsData;

    public UnityDataProvider getUnityDataProvider(int dateIndex) {
        return programsData.get(dateIndex);
    }

    public DataProvider() {
        programsData = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            UnityDataProvider list = new UnityDataProvider();
            programsData.add(list);
        }
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < programsData.size(); i++) {
            UnityDataProvider list = programsData.get(i);
            ParseQuery query = Timeslot.getQuery();
            query.fromLocalDatastore();
            query.fromPin(Timeslot.PIN_TAG);
            query.whereEqualTo("program_id", i + 1);
            query.orderByAscending("timeslot_id");

            // groupItems
            List<Timeslot> results = null;
            try {
                results = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("XXXX!!!!@@@@@@@: ", "results.size: " + results.size());

            for (int j = 0; j < results.size(); j++) {
                final long groupId = j;
                final UnityDataProvider.ConcreteGroupData group = new UnityDataProvider.ConcreteGroupData(groupId, results.get(j).getValue());
                final List<UnityDataProvider.ConcreteChildData> children = new ArrayList<>();
                query = Session_Timeslot.getQuery();
                query.fromLocalDatastore();
                query.fromPin(Session_Timeslot.PIN_TAG);
                query.whereEqualTo("timeslot_id", results.get(j).getTimeslotId());
                query.orderByAscending("session_id");
                //childItems
                List<Session_Timeslot> sessionResults = null;

                try {
                    sessionResults = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int k = 0; k < sessionResults.size(); k++) {
                    final long childId = group.generateNewChildId();
                    Session_Timeslot sessionResult = sessionResults.get(k);
                    children.add(new UnityDataProvider.ConcreteChildData(childId, sessionResult));
                }
                list.add(new Pair<UnityDataProvider.ConcreteGroupData, List<UnityDataProvider.ConcreteChildData>>(group, children));

            }


        }
    }

/*    public static final class SubDataProvider extends AbstractExpandableDataProvider {

        List<Pair<ConcreteGroupData, List<ConcreteChildData>>> mData;
        public SubDataProvider(){
            mData=new LinkedList<>();
        }

        public void add(Pair<ConcreteGroupData, List<ConcreteChildData>> pair){
            mData.add(pair);
        }

        @Override
        public int getGroupCount() {
            return mData.size();
        }

        @Override
        public int getChildCount(int groupPosition) {
            return mData.get(groupPosition).second.size();
        }

        @Override
        public ConcreteGroupData getGroupItem(int groupPosition) {
            if (groupPosition < 0 || groupPosition >= getGroupCount()) {
                throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
            }

            return mData.get(groupPosition).first;
        }

        @Override
        public ConcreteChildData getChildItem(int groupPosition, int childPosition) {
            if (groupPosition < 0 || groupPosition >= getGroupCount()) {
                throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
            }

            final List<ConcreteChildData> children = mData.get(groupPosition).second;

            if (childPosition < 0 || childPosition >= children.size()) {
                throw new IndexOutOfBoundsException("childPosition = " + childPosition);
            }

            return children.get(childPosition);
        }


        public static final class ConcreteGroupData extends GroupData {

            private final long groupId;
            private final String text;
            private long mNextChildId;

            public ConcreteGroupData(long groupId, String text) {
                this.groupId = groupId;
                this.text = text;
                mNextChildId = 0;
                // for future
            }

            @Override
            public long getGroupId() {
                return groupId;
            }

            @Override
            public String getText() {
                return text;
            }

            public long generateNewChildId() {
                final long id = mNextChildId;
                mNextChildId += 1;
                return id;
            }
        }

        public static class ConcreteChildData extends ChildData {
            private long childId;
            private final Session_Timeslot session_timeslot;
            private boolean selectedTag;
            private List<Paper> papers;

            public ConcreteChildData(long childId, Session_Timeslot session_timeslot) {
                this.childId = childId;
                this.session_timeslot = session_timeslot;
                this.selectedTag = session_timeslot.getSelected() == 0 ? false : true;

                String pNames = session_timeslot.getPapers();

                //populate papers
                if (pNames != null && !pNames.isEmpty() && !pNames.trim().equals("0")) {
                    pNames.trim();
                    String[] paperNames = pNames.split(",");
                    ParseQuery<Paper> query = Paper.getQuery();
                    query.fromLocalDatastore();
                    query.whereContainedIn("unique_id", new ArrayList<String>(Arrays.asList(paperNames)));
                    query.fromPin(Paper.PIN_TAG);
                    try {
                        papers = query.find();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public long getChildId() {
                return this.childId;
            }

            public String getFirstText() {
                return session_timeslot.getValue();
            }

            public String getSecondText() {
                return session_timeslot.getRoom();
            }

            public String getChair() {
                return session_timeslot.getChair();
            }

            public boolean getSelected() {
                return selectedTag;
            }

            public void setSelected(boolean flag) {
                this.selectedTag = flag;
                session_timeslot.setSelected(flag ? 1 : 0);
                session_timeslot.pinInBackground(Session_Timeslot.PIN_TAG);
            }


            @Deprecated
            @Override
            public String getText() {
                return null;
            }

            public List<Paper> getPapers() {
                return papers;
            }
        }

    }*/
}
