<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Task - Personal Task Tracker</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Add New Task</h1>
        <a href="/tasks" class="btn btn-secondary">Back to Tasks</a>
    </header>

    <form method="post" action="/tasks" class="task-form">
        <div class="form-group">
            <label for="title">Title *</label>
            <input type="text" id="title" name="title" required maxlength="255">
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="3" maxlength="1000"></textarea>
        </div>

        <div class="form-group">
            <label for="notes">Notes</label>
            <textarea id="notes" name="notes" rows="3" maxlength="2000" placeholder="Additional notes or comments..."></textarea>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="category">Category</label>
                <select id="category" name="category">
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat}" ${cat == 'PERSONAL' ? 'selected' : ''}>${cat.displayName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="priority">Priority</label>
                <select id="priority" name="priority">
                    <c:forEach var="p" items="${priorities}">
                        <option value="${p}" ${p == 'MEDIUM' ? 'selected' : ''}>${p.displayName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status">
                    <c:forEach var="s" items="${statuses}">
                        <option value="${s}" ${s == 'PENDING' ? 'selected' : ''}>${s.displayName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="dueDate">Due Date (Optional)</label>
                <input type="datetime-local" id="dueDate" name="dueDateString">
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Create Task</button>
            <a href="/tasks" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
