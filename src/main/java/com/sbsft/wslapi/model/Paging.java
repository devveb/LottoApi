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

    /** 검색 키워드 */
    private String searchKeyword;

    /** 검색 유형 */
    private String searchType;

    public Paging() {
        this.currentPageNo = 1;
        this.recordsPerPage = 30;
        this.pageSize = 10;
    }

    public Paging(int currentPageNo,String searchType){
        this.currentPageNo = currentPageNo;
        this.searchType = searchType;
        this.recordsPerPage = 30;
        this.pageSize = 10;
    }

    public int getStartPage() {
        return (currentPageNo - 1) * recordsPerPage;
    }
}
