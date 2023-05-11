package gui;

/**
 * The PipeImage Enum stores the file paths of the pipe images
 */
public enum PipeImage {
    WALL("wall","jpg"),
    END_PIPE("endPipe","jpg"),
    CURVE_PIPE("curvePipe","jpg"),
    STRAIGHT_PIPE("straightPipe","jpg"),
    T_PIPE("tPipe","jpg"),
    SOURCE("wheel","png");

    private final String fileName;
    private final String fileType;

    PipeImage(String fileName, String fileType){
        this.fileName = fileName;
        this.fileType = fileType;
    }

    /**
     * to get the file path of a pipe image(not filled)
     *
     * @return the string of a pipe image file path
     */
    public String getFilePath(){
        return "/gui/pipes/" + fileName + "." + fileType;
    }

    /**
     * to get the file path of a pipe image
     *
     * @param isFilled true, to get the filled image; false, not filled image
     * @return the string of a pipe image file path
     */
    public String getFilePath(boolean isFilled) {
        StringBuilder filePath = new StringBuilder().append("/gui/pipes/").append(fileName);

        if(isFilled) {
            if ((this != PipeImage.WALL) && (this != PipeImage.SOURCE))
                filePath.append("_filled");
        }
        filePath.append(".").append(fileType);

        return filePath.toString();
    }
}
