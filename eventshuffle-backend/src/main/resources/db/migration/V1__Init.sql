CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE, -- name must be unique,
    version BIGSERIAL NOT NULL
);

CREATE TABLE event_date (
    id BIGSERIAL PRIMARY KEY,
    proposed_date DATE NOT NULL,
    event_id BIGINT NOT NULL REFERENCES events(id),
    version BIGSERIAL NOT NULL
);

CREATE TABLE event_date_vote (
    id BIGSERIAL PRIMARY KEY,
    voting_person VARCHAR(100) NOT NULL,
    event_date_id BIGINT NOT NULL REFERENCES event_date(id),
    version BIGSERIAL NOT NULL
);

/* [jooq ignore start] */
-- only unique voters allowed for proposed dates
CREATE UNIQUE INDEX unique_event_date_voter_idx on event_date_vote using btree (event_date_id, voting_person);

-- only unique proposed dates allowed for events
CREATE UNIQUE INDEX unique_proposed_date_on_event_idx on event_date using btree (event_id, proposed_date);
/* [jooq ignore stop] */