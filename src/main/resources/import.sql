insert into services (id, name, description, bindable) values ('074B2280-C5CC-4C9F-92E3-8042EB5681FE', 'SR-1', 'Service Repository 1', true)
insert into plans (id, service_name, name, description) values ('47B53538-F8EE-47B5-9E98-A3F2CECBEE07', 'SR-1', 'P-1', 'Plan 1');
insert into credentials (id, service_name, plan_name, key, value) values ('56848972-2CAB-4EC6-BD34-7BE5B97D0110', 'SR-1', 'P-1', 'Username', 'user');
insert into credentials (id, service_name, plan_name, key, value) values ('49F739BE-E2EF-4E14-9148-85745F3855DD', 'SR-1', 'P-1', 'Password', 'password');
insert into plans (id, service_name, name, description) values ('32D3DE52-898F-4E02-8CA0-98FD6277C28C', 'SR-1', 'P-2', 'Plan 2');

insert into services (id, name, description, bindable) values ('3582A12A-09F6-410B-BDB7-B81550F36BE5', 'SR-2', 'Service Repository 2', true)
insert into plans (id, service_name, name, description) values ('90CD725B-67EB-45CA-97AD-E97FD71464EE', 'SR-2', 'P-3', 'Plan 3');
insert into plans (id, service_name, name, description) values ('491B4AB7-5DF7-4E1C-9AF2-CA99FBD23BC9', 'SR-2', 'P-4', 'Plan 4');

















