package cmu.cconfs.utils.data;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cmu.cconfs.model.parseModel.Session_Room;

/**
 * Created by Wendy_Guo on 10/6/15.
 */
public class RoomDataProvider extends AbstractExpandableDataProvider {
    List<Pair<ConcreteGroupData, List<ConcreteChildData>>> mData;

    List<Pair<ConcreteGroupData, List<ConcreteChildData>>> mSelectedData;

    public RoomDataProvider() {
        mData = new LinkedList<>();
    }

    public void add(Pair<ConcreteGroupData, List<ConcreteChildData>> pair) {
        mData.add(pair);
    }

    ////////////////
    //for Schedule Activity
    public int getSelectedChildCount(int groudPosition) {
        return mSelectedData.get(groudPosition).second.size();
    }

    public int getSelectedGroupCount() {
        mSelectedData = new LinkedList<>();
        int length = mData.size();
        for (int i = 0; i < length; i++) {
            List<ConcreteChildData> childList = mData.get(i).second;
            int childLength = childList.size();
            List<ConcreteChildData> selectedChildList = new ArrayList<>();
            for (int j = 0; j < childLength; j++) {
                if (childList.get(j).getSelected()) {
                    selectedChildList.add(childList.get(j));
                }
            }
            if (selectedChildList.size() != 0) {
                Pair<ConcreteGroupData, List<ConcreteChildData>> pair = new Pair<>(mData.get(i).first, selectedChildList);
                mSelectedData.add(pair);
            }

        }

        return mSelectedData.size();
    }

    public ConcreteGroupData getSelectedGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getSelectedGroupCount()) {
            throw new IndexOutOfBoundsException("selectedGroupPosition = " + groupPosition);
        }

        return mSelectedData.get(groupPosition).first;
    }

    public ConcreteChildData getSelectedChildItem(int groupPosition, int childPosition) {

        if (groupPosition < 0 || groupPosition >= getSelectedGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        List<ConcreteChildData> children = mSelectedData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    ////////////////////////


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

        List<ConcreteChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }


    public static final class ConcreteGroupData extends GroupData {

        private final long groupId;
        private String text;
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
        private Session_Room session_room;
        private boolean selectedTag;
        // private List<Paper> papers;

        public ConcreteChildData(long childId, Session_Room session_room) {
            this.childId = childId;
            this.session_room = session_room;
            this.selectedTag = session_room.getSelected() == 0 ? false : true;

        }

        @Override
        public long getChildId() {
            return this.childId;
        }
        public String getFirstText() {
            return session_room.getSessionTitle();
        }
        public String getSecondText() {
            return session_room.getTimeslot();
        }


        public int getRoomId() {
            return session_room.getRoomId();
        }

        public String getChair() {
            return session_room.getChair();
        }

        public boolean getSelected() {
            return selectedTag;
        }

        public int getSessionId() {
            return session_room.getSessionId();
        }

        public String getSessionObjectId() {
            return session_room.getObjectId();
        }

        public String getSessionName() {
            return session_room.getSessionTitle();
        }

        public String getPaperIdArray() {
            return session_room.getPapers();
        }

//        public String getSessionRoom() {
//            return session_room.getRoom();
//        }

        public void setSelected(boolean flag) {
            this.selectedTag = flag;
            session_room.setSelected(flag ? 1 : 0);
            session_room.pinInBackground(Session_Room.PIN_TAG);
        }


        @Deprecated
        @Override
        public String getText() {
            return null;
        }
    }

}
