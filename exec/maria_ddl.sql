create or replace table member
(
    id               bigint auto_increment
        primary key,
    created_at       datetime(6) default current_timestamp(6) not null,
    updated_at       datetime(6) default current_timestamp(6) not null,
    credit_score     int         default 632                  not null,
    deleted          bit         default b'0'                 not null,
    image_url        varchar(255)                             null,
    name             varchar(32)                              null,
    oauth_identifier varchar(255)                             not null,
    phone_number     varchar(30)                              null,
    role             varchar(20) default 'UNKNOWN'            not null
);

create or replace table account
(
    deleted            bit         default b'0'                 not null,
    created_at         datetime(6) default current_timestamp(6) not null,
    id                 bigint auto_increment
        primary key,
    member_id          bigint                                   null,
    updated_at         datetime(6) default current_timestamp(6) not null,
    bank_code          varchar(20)                              not null,
    type               varchar(20)                              null,
    account_number     varchar(30)                              not null,
    fin_account_number varchar(40)                              null,
    constraint fk_account_to_member_member_id
        foreign key (member_id) references member (id)
);

create or replace table account_history
(
    account_id              bigint      not null,
    amount                  bigint      not null,
    api_created_at          datetime(6) not null,
    id                      bigint auto_increment
        primary key,
    transaction_partner_id  bigint      not null,
    transaction_identifier  binary(16)  not null,
    transaction_distinction varchar(20) not null,
    transaction_type        varchar(20) not null,
    constraint fk_account_history_to_account_account_id
        foreign key (account_id) references account (id),
    constraint fk_account_history_to_member_transaction_partner_id
        foreign key (transaction_partner_id) references member (id)
);

create or replace table credit_history
(
    credit_score int         default 632                  not null,
    created_at   datetime(6) default current_timestamp(6) not null,
    id           bigint auto_increment
        primary key,
    member_id    bigint                                   not null,
    updated_at   datetime(6) default current_timestamp(6) not null,
    constraint fk_credit_history_to_member_member_id
        foreign key (member_id) references member (id)
);

create or replace table `group`
(
    id        bigint auto_increment
        primary key,
    activated bit default b'0' not null,
    deleted   bit default b'0' not null,
    child_id  bigint           not null,
    parent_id bigint           not null,
    constraint UK_nnvs8jbvtrdiuw4gnghjmnfku
        unique (child_id),
    constraint fk_group_to_member_child_id
        foreign key (child_id) references member (id),
    constraint fk_group_to_member_parent_id
        foreign key (parent_id) references member (id)
);

create or replace table confiscation
(
    id            bigint auto_increment
        primary key,
    activated     bit         default b'1'                 not null,
    amount        bigint                                   not null,
    repaid_amount bigint      default 0                    not null,
    started_at    datetime(6) default current_timestamp(6) not null,
    group_id      bigint                                   not null,
    constraint fk_confiscation_to_group_group_id
        foreign key (group_id) references `group` (id)
);

create or replace table fund_management
(
    allowance_date    int               not null,
    confiscation_rate int    default 50 not null,
    tax_rate          int    default 50 not null,
    allowance_amount  bigint            not null,
    group_id          bigint            not null,
    id                bigint auto_increment
        primary key,
    loan_amount       bigint default 0  not null,
    loan_limit        bigint default 0  not null,
    constraint UK_n2u8qhnngti40no75rb9hgwbq
        unique (group_id),
    constraint fk_fund_management_to_group_group_id
        foreign key (group_id) references `group` (id)
);

create or replace table interest
(
    activated  bit         default b'0'                 not null,
    amount     bigint                                   not null,
    billed_at  date                                     not null,
    created_at datetime(6) default current_timestamp(6) not null,
    expired_at date                                     not null,
    group_id   bigint                                   not null,
    id         bigint auto_increment
        primary key,
    paid_at    datetime(6)                              null,
    updated_at datetime(6) default current_timestamp(6) not null,
    constraint fk_interest_to_group_group_id
        foreign key (group_id) references `group` (id)
);

create or replace table member_relationship
(
    id        bigint auto_increment
        primary key,
    activated bit default b'0' not null,
    deleted   bit default b'0' not null,
    child_id  bigint           not null,
    parent_id bigint           not null,
    constraint UK_k0w6om9wfwh3bvbbnl0baal4g
        unique (child_id),
    constraint fk_member_relationship_to_member_child_id
        foreign key (child_id) references member (id),
    constraint fk_member_relationship_to_member_parent_id
        foreign key (parent_id) references member (id)
);

create or replace table mission
(
    credit_score int                                      not null,
    color        varchar(6)                               not null,
    amount       bigint                                   not null,
    content      bigint                                   null,
    created_at   datetime(6) default current_timestamp(6) not null,
    expired_at   datetime(6)                              not null,
    group_id     bigint                                   not null,
    id           bigint auto_increment
        primary key,
    updated_at   datetime(6) default current_timestamp(6) not null,
    status       varchar(20) default 'PENDING'            not null,
    title        varchar(255)                             not null,
    constraint fk_mission_to_group_group_id
        foreign key (group_id) references `group` (id)
);

create or replace table nh_api_management
(
    created_at datetime(6) default current_timestamp(6) not null,
    id         bigint auto_increment
        primary key,
    is_tuno    bigint                                   not null,
    updated_at datetime(6) default current_timestamp(6) not null
);

create or replace table savings
(
    month          int                                      not null,
    created_at     datetime(6) default current_timestamp(6) not null,
    ended_at       date                                     null,
    expired_at     date                                     not null,
    group_id       bigint                                   not null,
    id             bigint auto_increment
        primary key,
    monthly_amount bigint                                   not null,
    my_amount      bigint                                   not null,
    parents_amount bigint                                   not null,
    started_at     date                                     not null,
    updated_at     datetime(6) default current_timestamp(6) not null,
    status         varchar(20) default 'PENDING'            not null,
    payment_count  int         default 0                    not null,
    total_amount   bigint      default 0                    not null,
    delay_count    int         default 0                    not null,
    constraint fk_savings_to_group_group_id
        foreign key (group_id) references `group` (id)
);

create or replace table savings_item
(
    amount     bigint       not null,
    id         bigint auto_increment
        primary key,
    savings_id bigint       not null,
    image_url  varchar(255) null,
    name       varchar(255) not null,
    constraint UK_9w6wgrodq9jdn4ljtxh9977ku
        unique (savings_id),
    constraint fk_savings_item_to_savings_savings_id
        foreign key (savings_id) references savings (id)
);

create or replace table tax
(
    activated  bit default b'0' not null,
    amount     bigint           not null,
    expired_at date             not null,
    group_id   bigint           not null,
    id         bigint auto_increment
        primary key,
    constraint fk_tax_to_group_group_id
        foreign key (group_id) references `group` (id)
);
