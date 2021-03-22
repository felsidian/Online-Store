-- noinspection SqlDialectInspectionForFile
-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO role(name)
VALUES ("admin"),
       ("user");

INSERT INTO user(name,
                 email,
                 password,
                 locale,
                 phone_number,
                 role_id,
                 blocked)
VALUES ("Yevhen Hryhoriev",
        "hryhoriev75@gmail.com",
        "7b97b3938265d4f9448b6d855b48b44bf16935bc14864820f530b3fb7140ad0c1b9834b14c6120a9f525d692b58eb714d90d8f2312384d29247584425827c0f0e16e647a4360801b9ee4b7b6753db27212345678",
        "uk",
        "",
        1,
        0),
       ("Jane Doe",
        "jane.doe@gmail.com",
        "a9aceb402bec607e9b87e2d25f3bd4e35756ace160069a2f8664cad93f368e9f1a716b29ede9ee64b4267a84b4b8390e78d7369b6dc45690961a5e91f16da2984cc12163e9afa063627bd11b1ef7c8f8",
        "ua",
        "+380501234567",
        2,
        0);