CREATE TABLE persons (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE events (
    event_id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE proposed_dates (
    proposed_date_id SERIAL PRIMARY KEY,
    event_date DATE,
    event_id BIGINT REFERENCES events(event_id)
);

CREATE TABLE votes (
    id SERIAL PRIMARY KEY,
    voting_person_id BIGINT REFERENCES persons(person_id),
    proposed_dates BIGINT REFERENCES proposed_dates(proposed_date_id)
);
