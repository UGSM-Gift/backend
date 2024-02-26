INSERT INTO personal_category (name, type, view_order)
VALUES ('독서', 'HOBBY', 1),
       ('음악', 'HOBBY', 2),
       ('요리/베이킹', 'HOBBY', 3),
       ('게임', 'HOBBY', 4),
       ('운동/스포츠', 'HOBBY', 5),
       ('캠핑', 'HOBBY', 6),
       ('청소/세탁', 'HOBBY', 7),
       ('OTT', 'HOBBY', 8);


INSERT INTO personal_category (name, type, view_order)
VALUES ('패션', 'INTEREST', 1),
       ('뷰티', 'INTEREST', 2),
       ('음식', 'INTEREST', 3),
       ('술', 'INTEREST', 4),
       ('인테리어', 'INTEREST', 5),
       ('건강', 'INTEREST', 6),
       ('문화', 'INTEREST', 7),
       ('게임', 'INTEREST', 8),
       ('육아', 'INTEREST', 9),
       ('반려동물/식물', 'INTEREST', 10),
       ('자동차', 'INTEREST', 11),
       ('디지털 기기', 'INTEREST', 12);


INSERT INTO personal_category (name, type, view_order)
VALUES ('피부', 'WORRY', 1),
       ('다이어트', 'WORRY', 2),
       ('요리', 'WORRY', 3),
       ('인테리어', 'WORRY', 4),
       ('반려동물 관리', 'WORRY', 5),
       ('생활 편의', 'WORRY', 6),
       ('육아', 'WORRY', 7);


insert into personal_category_question(content, category_id)
VALUES ('독서하면서 주로 마시는 음료가 있나요?', 1),
       ('어느 상황에서 음악을 많이 듣나요?', 2),
       ('자주 사용하는 음식 재료는 무엇인가요?', 3),
       ('어떤 운동을 하나요?', 5),
       ('집 청소할 때 어디를 가장 신경 많이 쓰나요?', 7),
       ('OTT 는 어디서 많이 보나요?', 8),
       ('영상을 보면서 주로 먹는 음식이 있나요?', 8),
       ('나의 패션에서 가장 중요한 부분은?', 9),
       ('외출할 때 의상 외 가장 신경쓰는 부분은?', 10),
       ('평소 식사 방법이 어떻게 되나요?', 11),
       ('전통주와 와인 중 어느 술을 더 선호하시나요?', 12),
       ('집에서 제일 많이 머무는 공간은 어디인가요?', 13),
       ('어떤 건강을 챙기고 싶나요?', 14),
       ('현재 유아동 자녀가 있거나 임신중인가요?', 17),
       ('아이가 있다면 주로 아이와 어떤 활동을 많이 하나요?', 17),
       ('어떤 동물/식물을 키우나요?', 18),
       ('피부가 예민한 편인가요?', 22),
       ('보습이 필요한 신체 부위가 있나요?', 22),
       ('식단, 운동 중 어떤 활동이 가장 힘이드나요?', 23),
       ('요리에 관련해 어떤 고민이 있나요?', 24),
       ('인테리어에 관련해 어떤 고민이 있나요?', 25),
       ('강아지, 고양이중 누구의 관리를 해야하나요?', 26),
       ('집안일 중 어떤 일이 가장 힘드나요?', 27),
       ('육아에 관련해 어떤 고민이 있나요?', 28);


-- 취미 질문에 대한 답변 --
INSERT INTO personal_category_question_choice(content, question_id)
VALUES ('커피', 1),
       ('차류', 1),
       ('음료', 1),
       ('없음', 1),
       ('출/퇴근', 2),
       ('공부', 2),
       ('여행', 2),
       ('샤워', 2),
       ('산책', 2),
       ('빵', 3),
       ('유제품', 3),
       ('고기', 3),
       ('조미료', 3),
       ('헬스', 4),
       ('요가/필라테스', 4),
       ('골프', 4),
       ('기타', 4),
       ('거실/방', 5),
       ('화장실', 5),
       ('빨래', 5),
       ('주방', 5),
       ('거실', 6),
       ('방', 6),
       ('대중교통', 6),
       ('카페', 6),
       ('헬스장', 6),
       ('밥', 7),
       ('디저트/간식', 7),
       ('음료만', 7),
       ('디저트', 7);


INSERT INTO personal_category_question_choice(content, question_id)
VALUES ('옷', 8),
       ('악세서리', 8),
       ('신발', 8),
       ('가방', 8),
       ('헤어', 8),
       ('메이크업', 9),
       ('머리 스타일', 9),
       ('향기', 9),
       ('네일', 9),
       ('직접 조리', 10),
       ('조리 식품', 10),
       ('외식', 10),
       ('배달', 10),
       ('전통주', 11),
       ('와인', 11),
       ('주방', 12),
       ('서재', 12),
       ('거실/방', 12),
       ('화장실', 12),
       ('신체', 13),
       ('뷰티/미용', 13),
       ('다이어트', 13),
       ('임신중', 14),
       ('자녀가 있음', 14),
       ('공부', 14),
       ('놀이', 14),
       ('산책', 14),
       ('운동', 14),
       ('강아지', 15),
       ('고양이', 15),
       ('앵무새/햄스터', 15),
       ('식물', 15);


INSERT INTO personal_category_question_choice(content, question_id)
VALUES ('네', 17),
       ('아니오', 17),
       ('얼굴', 18),
       ('손', 18),
       ('바디', 18),
       ('식단 조절', 19),
       ('운동', 19),
       ('강아지', 22),
       ('고양이', 22),
       ('설거지', 23),
       ('청소', 23),
       ('세탁', 23),
       ('물건 수납/정리', 23);



INSERT INTO personal_category (name, type, view_order)
VALUES ('학생', 'OCCUPATION', 1),
       ('직장인', 'OCCUPATION', 2),
       ('자영업자', 'OCCUPATION', 3),
       ('프리랜서', 'OCCUPATION', 4),
       ('무직', 'OCCUPATION', 5),
       ('기타', 'OCCUPATION', 6);



INSERT INTO naver_shopping_category(id, name, parent_category_id, is_active)
VALUES(10011410, '캔들/디퓨저', null, true),
      (10011412, '블루투스스피커', null, true);

UPDATE naver_shopping_category
SET name = '키즈 주얼리' where id = 10014552;

UPDATE naver_shopping_category
SET name = '키즈 수영용품' where id = 10014553;

UPDATE naver_shopping_category
SET name = '키즈 주얼리' where id = 10014552;

UPDATE naver_shopping_category
SET name = '키즈 신발/가방/양말' where id = 10014554;

UPDATE naver_shopping_category
SET name = '키즈 외출용품' where id = 10014555;

UPDATE naver_shopping_category
SET name = '키즈 실내용품' where id = 10014635;

UPDATE naver_shopping_category
SET name = '키즈 목욕/위생용품' where id = 10014710;


UPDATE naver_shopping_category
SET name = '키즈 간식' where id = 10014873;

UPDATE naver_shopping_category
SET name = '신생아 의류' where id = 10015297;

UPDATE naver_shopping_category
SET name = '키즈 의류' where id = 10015298;

UPDATE naver_shopping_category
SET name = '키즈 속옷/파자마' where id = 10015299;

UPDATE naver_shopping_category
SET name = '키즈 코스튬/드레스' where id = 10015365;

UPDATE naver_shopping_category
SET name = '반려동물 패션' where id = 10012019;

UPDATE naver_shopping_category
SET name = '반려동물 외출용품' where id = 10012020;

UPDATE naver_shopping_category
SET name = '반려동물 실내용품' where id = 10012179;

UPDATE naver_shopping_category
SET name = '강아지 사료' where id = 10015019;

UPDATE naver_shopping_category
SET name = '강아지 간식' where id = 10015020;

UPDATE naver_shopping_category
SET name = '강아지 장난감' where id = 10015092;

UPDATE naver_shopping_category
SET name = '강아지 관리 용품' where id = 10015093;

UPDATE naver_shopping_category
SET name = '고양이 사료' where id = 10015094;

UPDATE naver_shopping_category
SET name = '고양이 장난감' where id = 10015096;

UPDATE naver_shopping_category
SET name = '고양이 관리 용품' where id = 10015161;

UPDATE naver_shopping_category
SET name = '앵무새/햄스터 용품' where id = 10012182;


with recursive shopping_category AS (
    SELECT id, name::character varying(255), 1 AS depth, parent_category_id
    FROM naver_shopping_category
    WHERE
        naver_shopping_category.parent_category_id is null
      and naver_shopping_category.is_active
      AND id NOT IN (10030807,10011397,10030841,10016442,10016443,10016548,10016445,10016447,10016448,10016546,10016449)
    UNION ALL
    SELECT naver_shopping_category.id, concat(shopping_category.name || ' > ' || naver_shopping_category.name)::character varying(255) , depth + 1, naver_shopping_category.parent_category_id
    FROM shopping_category
             JOIN naver_shopping_category ON naver_shopping_category.parent_category_id = shopping_category.id
    WHERE naver_shopping_category.is_active
--     JOIn naver_shopping_category "n2" ON n2.parent_category_id != shopping_category.id
)

SELECT s1.* FROM shopping_category "s1"
                     left outer join naver_shopping_category "nsc" on nsc.id = s1.parent_category_id
WHERE not exists(SELECT 1 FROM shopping_category "s2" WHERE s1.id = s2.parent_category_id);