insert into services (id, name, description, bindable) values ('074B2280-C5CC-4C9F-92E3-8042EB5681FE', 'ServiceRepository', 'Service Repository', true)
insert into plans (id, service_name, name, description) values ('47B53538-F8EE-47B5-9E98-A3F2CECBEE07', 'ServiceRepository', 'DummyPlan', 'Dummy Plan');
insert into credentials (id, service_name, plan_name, key, value) values ('56848972-2CAB-4EC6-BD34-7BE5B97D0110', 'ServiceRepository', 'DummyPlan', 'CredentialKey1', 'Credential Value 1');
insert into credentials (id, service_name, plan_name, key, value) values ('49F739BE-E2EF-4E14-9148-85745F3855DD', 'ServiceRepository', 'DummyPlan', 'CredentialKey2', 'Credential Value 2');
