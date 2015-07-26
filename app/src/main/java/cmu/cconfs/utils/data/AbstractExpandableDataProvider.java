package cmu.cconfs.utils.data;

/**
 * Created by zmhbh on 7/28/15.
 */
public abstract class AbstractExpandableDataProvider {

    public static abstract class BaseData {

        public abstract String getText();

    }

    public static abstract class GroupData extends BaseData {

        public abstract long getGroupId();
    }

    public static abstract class ChildData extends BaseData {
        public abstract long getChildId();
    }

    public abstract int getGroupCount();

    public abstract int getChildCount(int groupPosition);

    public abstract GroupData getGroupItem(int groupPosition);

    public abstract ChildData getChildItem(int groupPosition, int childPosition);


}
