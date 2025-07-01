# 任务管理模块改动说明

## 1. 前端主要改动

- 新增/编辑任务弹窗表单，支持任务的新增和编辑，字段与后端实体一致。
- 完善 handleAdd、handleEdit、submitEditForm 逻辑，调用后端API实现任务的增删改查。
- 导出功能实现，调用后端导出接口，下载Excel文件。
- 优化表单校验，防止必填字段为空。
- 任务详情、编辑弹窗区分，体验更友好。

## 2. 后端主要改动

- TaskInfoController 新增 /api/tasks/export 导出接口，返回Excel文件流。
- create/update 接口增加参数校验，防止必填字段为空。
- pom.xml 增加 Apache POI 依赖用于Excel导出。

## 3. 主要接口说明

- GET /api/tasks 分页查询任务
- GET /api/tasks/all 查询所有任务
- GET /api/tasks/{id} 查询任务详情
- POST /api/tasks 新增任务（参数校验）
- PUT /api/tasks/{id} 编辑任务（参数校验）
- DELETE /api/tasks/{id} 删除任务
- GET /api/tasks/export 导出任务Excel

## 4. 兼容性与注意事项

- 前后端字段保持一致，避免遗漏。
- 导出功能需后端已引入POI依赖。
- 新增/编辑/导出等功能均有表单校验和异常处理。

---
如需进一步扩展（如软删除、人员下拉、权限控制等），可继续完善。 