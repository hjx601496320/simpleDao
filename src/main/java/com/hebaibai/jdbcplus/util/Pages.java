package com.hebaibai.jdbcplus.util;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页工具
 *
 * @param <T>
 */
public class Pages<T> {

    /**
     * 每一页保存的数据
     */
    @Getter
    @Setter
    private List<T> list;

    /**
     * 总记录条数
     */
    @Getter
    @Setter
    private int total;

    /**
     * 每一页的记录条数
     */
    @Getter
    @Setter
    private int pageSize;

    /**
     * 所有的页数
     */
    @Getter
    @Setter
    private int totalPages;

    /**
     * 当前页数
     */
    @Getter
    @Setter
    private int currentPage;

    /**
     * 是否为第一页
     */
    private boolean isFirstPage = false;
    /**
     * 是否为最后一页
     */
    private boolean isLastPage = false;
    /**
     * 是否有前一页
     */
    private boolean hasPreviousPage = false;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage = false;

    /**
     * 导航栏中的数量
     */
    private int navItemCount;

    /**
     * 导航栏中每一块的值
     */
    private int[] navItemsNum;


    /**
     * @param total
     * @param currentPage
     * @param pageSize
     */
    public Pages(int total, int currentPage, int pageSize) {
        this.total = total;
        this.pageSize = pageSize;
        this.totalPages = (this.total - 1) / this.pageSize + 1;
        //边界判定
        if (currentPage < 1) {
            this.currentPage = 1;
        } else if (currentPage > this.totalPages) {
            this.currentPage = this.totalPages;
        } else {
            this.currentPage = currentPage;
        }
        // 基本参数设定之后进行导航页面的计算
        calculate();
        // 以及页面边界的判定
        judgePageBoudary();
    }


    /**
     * 计算导航页
     */
    private void calculate() {
        // 当总页数小于或等于导航页码数时
        if (totalPages <= navItemCount) {
            navItemsNum = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navItemsNum[i] = i + 1;
            }
        } else { // 当总页数大于导航页码数时
            navItemsNum = new int[navItemCount];
            int startNum = currentPage - navItemCount / 2;
            int endNum = currentPage + navItemCount / 2;

            if (startNum < 1) {
                startNum = 1;
                // (最前navigatePages页
                for (int i = 0; i < navItemCount; i++) {
                    navItemsNum[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                // 最后navigatePages页
                for (int i = navItemCount - 1; i >= 0; i--) {
                    navItemsNum[i] = endNum--;
                }
            } else {
                // 所有中间页
                for (int i = 0; i < navItemCount; i++) {
                    navItemsNum[i] = startNum++;
                }
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = currentPage == 1;
        isLastPage = currentPage == totalPages && currentPage != 1;
        hasPreviousPage = currentPage > 1;
        hasNextPage = currentPage < totalPages;
    }

}
