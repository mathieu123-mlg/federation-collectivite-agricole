do
$$
    begin
        if not exists(select from pg_type where typname = 'attendance_status') then
            create type attendance_status as enum (
                'ATTENDED',
                'MISSING',
                'UNDEFINED');
        end if;
    end
$$;

create table if not exists "attendance"
(
    id                varchar primary key,
    activity_id       varchar references "activity" (id),
    member_id         varchar references "member" (id),
    attendance_status attendance_status default 'UNDEFINED',
    unique (activity_id, member_id)
);

alter type attendance_status rename value 'ATTENDED' to 'PRESENT';
