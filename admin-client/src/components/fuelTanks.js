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

export const FuelTankList = () => (
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
            <TextField source="capacity"/>
            <TextField source="fuelTankTypeId"/>
            <TextField source="fuelTankType.nameRu" label="base name"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const FuelTankEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="fuelTankId" reference="fuelTanks"/>
            <NumberInput disabled source="id"/>
            <TextInput source="characterName" disabled/>
            <TextInput source="fuelTankType.nameRu" disabled/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
            <NumberInput source="capacity"/>
            <TextInput source="nameRu" fullWidth/>
            <TextInput source="dscRu" fullWidth/>
        </SimpleForm>
    </Edit>
);


export const FuelTankCreate = () => {
    const {data: typesData} = useGetList('fuel_tank_types');
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
                    source="fuelTankTypeId"
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
        setValue('capacity', props.type.capacity);
    }, [setValue, props]);

    return (
        <div>
            <TextInput source="dscRu" fullWidth/>
            <TextInput source="nameRu" fullWidth/>
            <NumberInput source="cost"/>
            <NumberInput source="capacity"/>
        </div>
    );
};
