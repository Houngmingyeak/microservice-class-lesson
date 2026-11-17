--liquibase formatted sql
--changeset istad:007-alter-picture-url-to-text
ALTER TABLE users ALTER COLUMN picture_url TYPE TEXT;
