set @ovenId = 'elux-oven';
set @washerId = 'wash-machine';

set @ovenStateSchema = '
{
    "$schema": "http://json-schema.org/draft-03/schema#",
    "type": "object",
    "properties": {
        "sound" : {
            "type": "string",
            "enum": ["on", "off"]
        }
    }
}
';

set @washStateSchema = '
{
    "$schema": "http://json-schema.org/draft-03/schema#",
    "type": "object",
    "properties": {
        "sound" : {
            "type": "string",
            "enum": ["on", "off"]
        }
    },
    "additionalProperties" : false
}
';

set @washerProgramParamScheme = '
  {
    "$schema": "http://json-schema.org/draft-03/schema#",
    "type": "object",
    "properties": {
        "sound" : {
            "type": "string",
            "enum": ["on", "off"]
        }
    },
    "additionalProperties" : false
  }
  ';

set @ovenProgramParamScheme = '
{
  "$schema" : "http://json-schema.org/draft-03/schema#",
  "type" : "object",
  "properties" : {
    "ventilation" : {
      "type" : "boolean"
    },
    "grill" : {
      "type" : "boolean",
      "required": true
    },
    "temp" : {
      "type" : "integer",
      "minimum" : 160,
      "maximum" : 250,
      "required": true
    }
  }
}
';

insert into endpoint (id, editable_state, type, state_schema)
values (
      @ovenId,
      '{"sound" : "off"}',
      'oven',
      @ovenStateSchema
),(
      @washerId,
      '{"sound" : "on"}',
      'wash machine ',
      @washStateSchema
);

insert into endpoint_program (
  endpoint_id,
  name,
  description,
  parameter_scheme
) values (
  @ovenId,
  'regular',
  'regular program that can be customized through parameters',
  @ovenProgramParamScheme
),(
  @washerId,
  'quick 30m wash',
  'quick washing for 30 minutes',
  @washerProgramParamScheme
),(
  @washerId,
  'regular 1h wash',
  'regular washing for 1 hour',
  @washerProgramParamScheme
),(
  @washerId,
  'intensive wash',
  'intensive washing for 2 hours',
  @washerProgramParamScheme
);