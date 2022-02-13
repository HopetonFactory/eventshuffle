CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE event_date (
    id BIGSERIAL PRIMARY KEY,
    proposed_date DATE NOT NULL,
    event_id BIGINT NOT NULL REFERENCES events(id)
);

CREATE TABLE event_date_vote (
    id BIGSERIAL PRIMARY KEY,
    voting_person VARCHAR(100) NOT NULL,
    event_date_id BIGINT NOT NULL REFERENCES event_date(id)
);

-- ADD unique index between voting person and event_date_id -> a single name can vote only once