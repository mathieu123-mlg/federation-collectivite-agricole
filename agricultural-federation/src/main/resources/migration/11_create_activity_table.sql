do
$$
    begin
        if not exists(select from pg_type where typname = 'activity_type') then
            create type activity_type as enum (
                'MEETING',
                'TRAINING',
                'OTHER');
        end if;
    end
$$;

create table if not exists "activity"
(
    id                    varchar primary key,
    collectivity_id       varchar references "collectivity" (id),
    label                 varchar,
    activity_type         activity_type,
    week_ordinal          integer,
    day_of_week           varchar,
    executive_date        date,
    occupations_concerned varchar
);
