package com.example.quentin.csg_qualifs_18;

public class Note {

    private Long _id;
    private String _note;
    private String _timeStamp;
    private Integer _imagePosition;

    public Note(Long id, String note, String timeStamp, Integer imagePosition) {
        _id = id;
        _note = note;
        _timeStamp = timeStamp;
        _imagePosition = imagePosition;
    }

    public Long getId() { return _id; }

    public String getNote() { return _note; }

    public String getTimeStamp() {
        return _timeStamp;
    }

    public Integer getImagePosition() {
        return _imagePosition;
    }

}
