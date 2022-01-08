package com.sbsft.wslapi.model;


import lombok.Data;

@Data
public class Paging {
    /** 현재 페이지 번호 */
    private int currentPageNo;

    /** 페이지당 출력할 데이터 개수 */
    private int recordsPerPage;

    /** 화면 하단에 출력할 페이지 사이즈 */
    private int pageSize;

    /** 총 페이지 수 **/
    private int totalPages;

    /** present page block **/
    private int presentPageBlock;
    private int presentPageBlockStart;
    private int presentPageBlockEnd;



    public Paging() {
        this.currentPageNo = 1;
        this.recordsPerPage = 30;
        this.pageSize = 10;
    }

    public Paging(int currentPageNo){
        this.currentPageNo = currentPageNo;
        this.recordsPerPage = 30;
        this.pageSize = 10;
        this.presentPageBlock = currentPageNo/pageSize;
        this.presentPageBlockStart = presentPageBlock * pageSize;


    }



    public int getStartPage() {
        return (currentPageNo - 1) * recordsPerPage;
    }

    public void setTotalPages(int totalRecord) {

        this.totalPages = totalRecord/this.recordsPerPage+1;
        this.presentPageBlockEnd = (presentPageBlockStart + pageSize < this.totalPages)?presentPageBlockStart + pageSize:this.totalPages;
    }




}

