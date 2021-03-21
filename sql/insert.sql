# noinspection SqlNoDataSourceInspectionForFile

INSERT INTO role(name)
VALUES("admin");

INSERT INTO role(name)
VALUES("user");

INSERT INTO user(
    name,
    email,
    password,
    salt,
    phone_number,
    role_id,
    blocked)
VALUES(
          "Andrii",
          "andrii777@ukr.mail",
          "AAABABBBABBABABBABABABABBABABBAAABABBBABBABABBABABABABBABABB",
          "AAABABBBABBABABBABABABABBABABB",
          "",
          1,
          0);