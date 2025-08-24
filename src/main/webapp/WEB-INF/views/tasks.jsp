<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Personal Task Tracker</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Personal Task Tracker</h1>
        <a href="/tasks/new" class="btn btn-primary">Add New Task</a>
    </header>

    <!-- Success Message -->
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>

    <!-- Search and Filter Section -->
    <div class="filters-section">
        <form method="get" action="/tasks" class="filters-form">
            <div class="search-group">
                <input type="text" name="search" placeholder="Search tasks..." value="${search}" class="search-input">
                <button type="submit" class="btn btn-primary">Search</button>
            </div>

            <div class="filter-group">
                <select name="status" class="filter-select">
                    <option value="">All Status</option>
                    <c:forEach var="s" items="${statuses}">
                        <option value="${s}" ${status == s ? 'selected' : ''}>${s.displayName}</option>
                    </c:forEach>
                </select>

                <select name="priority" class="filter-select">
                    <option value="">All Priority</option>
                    <c:forEach var="p" items="${priorities}">
                        <option value="${p}" ${priority == p ? 'selected' : ''}>${p.displayName}</option>
                    </c:forEach>
                </select>

                <select name="category" class="filter-select">
                    <option value="">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat}" ${category == cat ? 'selected' : ''}>${cat.displayName}</option>
                    </c:forEach>
                </select>

                <select name="sortBy" class="filter-select">
                    <option value="createdDate" ${sortBy == 'createdDate' ? 'selected' : ''}>Sort by Created Date</option>
                    <option value="dueDate" ${sortBy == 'dueDate' ? 'selected' : ''}>Sort by Due Date</option>
                    <option value="priority" ${sortBy == 'priority' ? 'selected' : ''}>Sort by Priority</option>
                    <option value="title" ${sortBy == 'title' ? 'selected' : ''}>Sort by Title</option>
                </select>
            </div>

            <div class="filter-actions">
                <button type="submit" class="btn btn-secondary">Apply Filters</button>
                <a href="/tasks" class="btn btn-outline">Clear All</a>
            </div>
        </form>
    </div>

    <div class="task-stats">
        <div class="stat">
            <h3>Total Tasks: ${tasks.size()}</h3>
        </div>
    </div>

    <!-- Bulk Operations -->
    <c:if test="${not empty tasks}">
        <div class="bulk-operations">
            <div class="bulk-controls">
                <button type="button" class="btn btn-secondary" onclick="toggleSelectAll()">Select All</button>
                <span id="selected-count">0 selected</span>
            </div>

            <div class="bulk-actions" id="bulk-actions" style="display: none;">
                <form method="post" action="/tasks/bulk-delete" style="display: inline;"
                      onsubmit="return confirmBulkDelete()">
                    <input type="hidden" name="selectedTasks" id="bulkDeleteTasks">
                    <button type="submit" class="btn btn-danger">Delete Selected</button>
                </form>

                <form method="post" action="/tasks/bulk-status" style="display: inline;">
                    <input type="hidden" name="selectedTasks" id="bulkStatusTasks">
                    <select name="bulkStatus" class="bulk-select">
                        <option value="PENDING">Mark as Pending</option>
                        <option value="IN_PROGRESS">Mark as In Progress</option>
                        <option value="COMPLETED">Mark as Completed</option>
                    </select>
                    <button type="submit" class="btn btn-secondary">Update Status</button>
                </form>

                <form method="post" action="/tasks/bulk-priority" style="display: inline;">
                    <input type="hidden" name="selectedTasks" id="bulkPriorityTasks">
                    <select name="bulkPriority" class="bulk-select">
                        <option value="LOW">Set Low Priority</option>
                        <option value="MEDIUM">Set Medium Priority</option>
                        <option value="HIGH">Set High Priority</option>
                    </select>
                    <button type="submit" class="btn btn-secondary">Update Priority</button>
                </form>
            </div>
        </div>
    </c:if>

    <div class="tasks-container">
        <c:choose>
            <c:when test="${empty tasks}">
                <div class="no-tasks">
                    <p>No tasks found. <a href="/tasks/new">Create your first task!</a></p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="task-grid">
                    <c:forEach var="task" items="${tasks}">
                        <div class="task-card ${task.status.toString().toLowerCase()}">
                            <div class="task-checkbox">
                                <input type="checkbox" class="task-select" value="${task.id}">
                            </div>

                            <div class="task-header">
                                <h3>${task.title}</h3>
                                <div class="task-badges">
                                    <span class="category-badge" style="background-color: ${task.category.color}">${task.category.displayName}</span>
                                    <span class="priority priority-${task.priority.toString().toLowerCase()}">${task.priority.displayName}</span>
                                </div>
                            </div>

                            <div class="task-body">
                                <p class="description">${task.description}</p>

                                <c:if test="${not empty task.notes}">
                                    <div class="task-notes">
                                        <strong>Notes:</strong>
                                        <p>${task.notes}</p>
                                    </div>
                                </c:if>

                                <div class="task-meta">
                                    <p><strong>Status:</strong> ${task.status.displayName}</p>
                                    <p><strong>Created:</strong> ${task.createdDate}</p>
                                    <c:if test="${task.dueDate != null}">
                                        <p><strong>Due:</strong> ${task.dueDate}</p>
                                    </c:if>
                                </div>
                            </div>

                            <div class="task-actions">
                                <a href="/tasks/${task.id}/edit" class="btn btn-secondary">Edit</a>
                                <c:if test="${task.status != 'COMPLETED'}">
                                    <form method="post" action="/tasks/${task.id}/complete" style="display: inline;">
                                        <button type="submit" class="btn btn-success">Complete</button>
                                    </form>
                                </c:if>
                                <form method="post" action="/tasks/${task.id}/delete" style="display: inline;"
                                      onsubmit="return confirm('Are you sure you want to delete this task?')">
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    let selectedTasks = new Set();

    function updateSelectedCount() {
        const count = selectedTasks.size;
        document.getElementById('selected-count').textContent = count + ' selected';
        document.getElementById('bulk-actions').style.display = count > 0 ? 'flex' : 'none';

        // Update hidden inputs
        const tasksArray = Array.from(selectedTasks);
        document.getElementById('bulkDeleteTasks').value = tasksArray.join(',');
        document.getElementById('bulkStatusTasks').value = tasksArray.join(',');
        document.getElementById('bulkPriorityTasks').value = tasksArray.join(',');
    }

    function toggleSelectAll() {
        const checkboxes = document.querySelectorAll('.task-select');
        const allSelected = selectedTasks.size === checkboxes.length;

        checkboxes.forEach(cb => {
            cb.checked = !allSelected;
            if (!allSelected) {
                selectedTasks.add(cb.value);
            } else {
                selectedTasks.delete(cb.value);
            }
        });

        if (allSelected) selectedTasks.clear();
        updateSelectedCount();
    }

    function confirmBulkDelete() {
        return confirm('Are you sure you want to delete ' + selectedTasks.size + ' selected task(s)?');
    }

    // Add event listeners to checkboxes
    document.addEventListener('DOMContentLoaded', function() {
        const checkboxes = document.querySelectorAll('.task-select');
        checkboxes.forEach(cb => {
            cb.addEventListener('change', function() {
                if (this.checked) {
                    selectedTasks.add(this.value);
                } else {
                    selectedTasks.delete(this.value);
                }
                updateSelectedCount();
            });
        });
    });
</script>
</body>
</html>


