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
            <TextField source="item.id" label="id"/>
            <TextField source="item.equipped" label="equipped"/>
            <TextField source="item.slotId" label="slot"/>
            <TextField source="item.characterName" label="character"/>
            <TextField source="item.itemTypeId" label="item type"/>
            <TextField source="item.upgradeLevel" label="level"/>
            <TextField source="item.nameRu" label="name"/>
            <TextField source="item.dscRu" label="dsc"/>
            <TextField source="item.cost" label="cost"/>
            <TextField source="speed"/>
            <TextField source="jump"/>
            <TextField source="engineTypeId"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const EngineEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="engineId" reference="engines"/>
            <NumberInput disabled source="id"/>
            <TextInput source="characterName"/>
            <NumberInput source="itemTypeId"/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
            <NumberInput source="speed"/>
            <NumberInput source="jump"/>
        </SimpleForm>
    </Edit>
);

export const EngineCreate = () => (
    <Create>
        <SimpleForm>
            <ReferenceInput source="engineId" reference="engines"/>
            <NumberInput source="slotId"/>
            <TextInput source="characterName"/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
            <NumberInput source="speed"/>
            <NumberInput source="jump"/>
            <TextInput source="name" label="name"/>
            <TextInput source="dsc" label="dsc"/>
            <NumberInput source="engineTypeId"/>
        </SimpleForm>
    </Create>
);