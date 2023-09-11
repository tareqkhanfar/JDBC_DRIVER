package com.khanfar.MetaData;

    public class Page {
        private int pageNumber;
        private int pageSize;

        public Page(int pageNumber, int pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
