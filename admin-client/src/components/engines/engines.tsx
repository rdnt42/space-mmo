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
            <TextField source="id"/>
            <TextField source="isActive"/>
            <TextField source="characterName"/>
            <TextField source="engineTypeId"/>
            <TextField source="speed"/>
            <TextField source="upgradeLevel"/>
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
            <NumberInput source="engineTypeId"/>
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
            <TextInput source="characterName"/>
            <NumberInput source="engineTypeId"/>
            <NumberInput source="speed"/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
        </SimpleForm>
    </Create>
);