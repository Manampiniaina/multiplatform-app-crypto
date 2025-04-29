
DO $$
DECLARE
    table_name text;
    sequence_name text;
    disable_constraints_sql text;
    delete_data_sql text;
    enable_constraints_sql text;
    reset_sequences_sql text;
BEGIN
    -- Désactiver les contraintes de clé étrangère
    disable_constraints_sql := '';
    FOR table_name IN
        SELECT tablename FROM pg_tables WHERE schemaname = 'public'
    LOOP
        disable_constraints_sql := disable_constraints_sql || 'ALTER TABLE ' || table_name || ' DISABLE TRIGGER ALL; ';
    END LOOP;
    EXECUTE disable_constraints_sql;

    -- Supprimer les données de toutes les tables
    delete_data_sql := '';
    FOR table_name IN
        SELECT tablename FROM pg_tables WHERE schemaname = 'public'
    LOOP
        delete_data_sql := delete_data_sql || 'DELETE FROM ' || table_name || '; ';
    END LOOP;
    EXECUTE delete_data_sql;

    -- Réactiver les contraintes de clé étrangère
    enable_constraints_sql := '';
    FOR table_name IN
        SELECT tablename FROM pg_tables WHERE schemaname = 'public'
    LOOP
        enable_constraints_sql := enable_constraints_sql || 'ALTER TABLE ' || table_name || ' ENABLE TRIGGER ALL; ';
    END LOOP;
    EXECUTE enable_constraints_sql;

    -- Réinitialiser toutes les séquences à 1
    reset_sequences_sql := '';
    FOR sequence_name IN
        SELECT seq.sequence_name
        FROM information_schema.sequences seq
        WHERE seq.sequence_schema = 'public'
    LOOP
        reset_sequences_sql := reset_sequences_sql || 'ALTER SEQUENCE ' || sequence_name || ' RESTART WITH 1; ';
    END LOOP;
    EXECUTE reset_sequences_sql;
END $$;