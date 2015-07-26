package cmu.cconfs.model;

import cmu.cconfs.model.parseModel.Version;

/**
 * Created by zmhbh on 8/25/15.
 */
public class CheckVersion implements Comparable<Version> {

    private Version version;

    public CheckVersion(Version version) {
        this.version = version;
    }

    @Override
    public int compareTo(Version that) {
        if (that == null)
            return 1;

        String[] thisParts = this.version.getVersion().split("\\.");
        String[] thatParts = that.getVersion().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);

        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart)
                return -1;
            if (thisPart > thatPart)
                return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (that == null)
            return false;

        if (this.getClass() != that.getClass())
            return false;

        return this.compareTo((Version) that) == 0;
    }
}
