INSERT INTO customers (id, username, password, first_name, last_name, telephone, address, points)
VALUES(1, 'johnny', '123', 'Johnny', 'Nguyen', '012345634', '123 Add Street', 200);

INSERT INTO rewards VALUES(1, '10OFF', 10, '2021-01-02', '2021-03-20', 1); 

INSERT INTO bookings VALUES(1, '2021-01-02 20:30','20:30','2021-01-02', 'Window 1', 1, 1);



INSERT INTO rewards VALUES(2, '20OFF', 20, '2021-02-13', '2021-02-15', 1);
INSERT INTO rewards VALUES(3, '15OFF', 15, '2021-01-20', '2021-02-15', 1);
INSERT INTO rewards VALUES(4, 'NONE0', 0, '2021-01-20', '2021-02-15', 1);



INSERT INTO managers VALUES(1, 'M101@manager.com', 'M_101', '123', 'Michael', 'Engineering', '044469696');
INSERT INTO staffs VALUES(1, 'S_101', '123', 'S101@staff.com', 'Akhil', 'Kasina', '04123423444', 'Description Here', 20);

INSERT INTO items VALUES(1, 'PORK ST LOUIS', 35.9, 'lunch', 'Pork St. Louis Ribs offer the classic ribs look and taste – only ramped up to exceed your flavour expectations.');
INSERT INTO items VALUES(2, 'BEEF FULL RACK', 45.9, 'dinner', 'We know, we know, our Beef Ribs are so mouthwateringly beautiful that you simply have to snap a photo.');
INSERT INTO items VALUES(3, 'OLD SCHOOL CHEESE', 11.9, 'lunch', 'The Old School Cheese is a timeless classic. And you do not mess with timeless classics. Especially when they taste this good.');
INSERT INTO items VALUES(4, 'BUTCHER’S ORIGINAL', 11.9, 'dinner', 'It’s hard to go past the Butcher’s Original burger. This classic beef patty is the product of countless tireless hours of taste testing, and many more hours sourcing some of the most premium cuts of meat in Australia.');
INSERT INTO items VALUES(5, 'BACON & CHEESE', 14.9, 'lunch', 'We have found one way to make our beef burger even better – more meat.');
INSERT INTO items VALUES(6, 'THE AUSSIE', 17.9, 'lunch', 'So iconic that it could be on the flag, The Aussie is a national treasure. One that tastes remarkably of flame-grilled beef patty, bacon, pineapple, and beetroot');
INSERT INTO items VALUES(7, 'SIRLOIN STEAK ROLL', 19.9, 'dinner', 'Do you ever struggle to choose between an epic sirloin steak, and a hearty burger? Well do we have some exceptionally great news for you');
INSERT INTO items VALUES(8, 'CRISPY LOUISIANA', 14.9, 'lunch', 'Some things are better left to the imagination. Not so for our Crispy Louisiana Burger – that’s a job for your taste buds.');
INSERT INTO items VALUES(9, 'VEGAN BEYOND BURGER', 15.9, 'dinner', 'We’ve finally done it - we’ve gone beyond Beyond Meat. This burger is vegan from top to bottom bun for an entire meal where you don’t need to substitute any of the deliciousness.');

INSERT INTO bookingitems VALUES(1, 1, 2, 4);
INSERT INTO bookingitems VALUES(2, 1, 4, 2);