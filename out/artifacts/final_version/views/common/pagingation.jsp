<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.btl_web.paging.Pageable" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    ArrayList<String> listPages = new ArrayList<>();
    listPages.add("1");
    Pageable pageable = (Pageable) request.getAttribute("pageable");
    int currentPage = pageable.getPage();
    int maxPages = pageable.getTotalPages();

    //Nhìn thấy tối đa 2 page bé hơn
    if(currentPage - 1 > 3)
        listPages.add("...");

    for(int i = 2; i < maxPages; i++)
    {
        if (Math.abs(currentPage - i) >= 0 && Math.abs(currentPage - i) <= 2)
            listPages.add(i + "");
    }

    //Nhìn thấy tối đa 2 page lớn hơn
    if(maxPages - currentPage > 3)
        listPages.add("...");

    if(maxPages > 1)
        listPages.add(maxPages + "");

    request.setAttribute("list_pages", listPages);
    request.setAttribute("current_page", currentPage + "");
%>
<c:forEach var="page" items="${list_pages}">
    <li class="page-item active">
        <a <c:if test="${page eq current_page}">style="background: red" </c:if> class="page-link" href="${home}${page}">${page}</a>
    </li>
</c:forEach>