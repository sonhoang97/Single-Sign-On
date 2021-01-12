package com.example.demooauth2.modelView.pageList;

import com.sun.jdi.InternalException;
import lombok.Getter;

import java.util.List;
@Getter
public class PageList<T> {
    public PageList(List<T> source, int pageIndex, int pageSize, int totalCount)
    {
        if (source == null)
        {
            throw new NullPointerException();
        }

        if (pageSize < 0)
        {
            throw new InternalException("pageSize must be greater than zero");
        }

        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.source = source;
    }
    private List<T> source;
    private int pageIndex;
    private int pageSize;
    private int totalCount;
}
