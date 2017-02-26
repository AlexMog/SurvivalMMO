-- Create entity template
-- CreateEntityTemplate(name, clientObjectId);
local TEMPLATE = CreateEntityTemplate("Test", 0);
local events = {};

TEMPLATE:setCollisionsBox(10, 10, 5);

-- TEMPLATE:registerRpc(rpcName, ...); ... = accepted types

function events:onCollisionEnter(...)
	
end;

function events:onCollisionExit()

end;

function events:onCollisionStay()
	
end

function events:onEnable()
	
end

function events:onDisable()

end

function events:onDestroy()

end

function events:start()

end

function events:update()

end

function events:awake()

end

-- TEMPLATE:RegisterEvent("awake", awake);

for k, v in pairs(events) do
	TEMPLATE:RegisterEvent(k, v); -- Register all events for which handlers have been defined
end

function RPC1()

end

-- ORDER IS IMPORTANT
TEMPLATE:registerRpc(RPC1, "Integer", "Boolean");