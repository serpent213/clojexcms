-- name: content-by-id
-- Get Markdown content entity
select * from content
  where id = :id
