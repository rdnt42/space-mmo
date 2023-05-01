import {
    Create,
    Datagrid,
    Edit,
    EditButton,
    List,
    NumberInput,
    ReferenceInput,
    SimpleForm,
    TextField,
    TextInput
} from "react-admin";

export const EngineList = () => (
    <List>
        <Datagrid>
            <TextField source="equipment.id"/>
            <TextField source="equipment.equipped"/>
            <TextField source="equipment.slotId"/>
            <TextField source="equipment.characterName"/>
            <TextField source="equipment.equipmentTypeId"/>
            <TextField source="equipment.upgradeLevel"/>
            <TextField source="speed"/>
            <TextField source="cost"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const EngineEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="engineId" reference="engines"/>
            <TextInput source="characterName"/>
            <NumberInput source="equipmentTypeId"/>
            <NumberInput source="speed"/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
        </SimpleForm>
    </Edit>
);

export const EngineCreate = () => (
    <Create>
        <SimpleForm>
            <ReferenceInput source="engineId" reference="engines"/>
            <NumberInput source="slotId"/>
            <TextInput source="characterName"/>
            <NumberInput source="equipmentTypeId"/>
            <NumberInput source="speed"/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
        </SimpleForm>
    </Create>
);