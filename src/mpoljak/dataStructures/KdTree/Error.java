package mpoljak.dataStructures.KdTree;

public enum Error {
    INVALID_DIMENSION(-1000),
    NULL_PARAMETER(-1001);

    private final int errCode;

    Error(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return this.errCode;
    }
}
