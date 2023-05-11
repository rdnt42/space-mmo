import {
    Create,
    Datagrid,
    Edit,
    EditButton,
    List,
    NumberInput,
    ReferenceInput,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
    useGetList,
} from "react-admin";
import {useEffect, useState} from "react";
import {useFormContext} from 'react-hook-form';

export const CargoHookList = () => (
    <List>
        <Datagrid>
            <TextField source="item.id" label="id"/>
            <TextField source="item.slotId" label="slot"/>
            <TextField source="item.characterName" label="character"/>
            <TextField source="item.itemTypeId" label="item type"/>
            <TextField source="item.upgradeLevel" label="level"/>
            <TextField source="item.nameRu" label="name"/>
            <TextField source="item.dscRu" label="dsc"/>
            <TextField source="item.cost" label="cost"/>
            <TextField source="loadCapacity"/>
            <TextField source="radius"/>
            <TextField source="cargoHookTypeId"/>
            <TextField source="cargoHookType.nameRu" label="base name"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const CargoHookEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="cargoHookId" reference="cargoHooks"/>
            <NumberInput disabled source="id"/>
            <TextInput source="characterName" disabled/>
            <NumberInput source="itemTypeId" disabled/>
            <NumberInput source="cargoHookTypeId" disabled/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
            <NumberInput source="loadCapacity"/>
            <NumberInput source="radius"/>
            <TextInput source="nameRu" fullWidth/>
            <TextInput source="dscRu" fullWidth/>
        </SimpleForm>
    </Edit>
);


export const CargoHookCreate = () => {
    const {data: typesData} = useGetList('cargo_hook_types');
    const [selectedType, setSelectedType] = useState({});

    if (!typesData) {
        return null;
    }

    const handleTypeChange = (event) => {
        for (const type of typesData) {
            if (type.id === event.target.value) {
                setSelectedType(type);
                break;
            }
        }
    };

    return (
        <Create>
            <SimpleForm>
                <AddPropInput type={selectedType}/>
                <TextInput source="characterName"/>
                <NumberInput source="upgradeLevel" defaultValue={0}/>
                <SelectInput
                    source="cargoHookTypeId"
                    choices={typesData}
                    optionText="nameRu"
                    optionValue="id"
                    label="type"
                    onChange={handleTypeChange}
                />
            </SimpleForm>
        </Create>
    );
};

const AddPropInput = (props) => {
    const {setValue} = useFormContext();

    useEffect(() => {
        setValue('dscRu', props.type.dscRu);
        setValue('nameRu', props.type.nameRu);
        setValue('cost', props.type.cost);
        setValue('loadCapacity', props.type.loadCapacity);
        setValue('radius', props.type.radius);
    }, [setValue, props]);

    return (
        <div>
            <TextInput source="dscRu" fullWidth/>
            <TextInput source="nameRu" fullWidth/>
            <NumberInput source="cost"/>
            <NumberInput source="loadCapacity"/>
            <NumberInput source="radius"/>
        </div>
    );
};
