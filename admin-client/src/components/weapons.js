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

export const WeaponList = () => (
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
            <TextField source="damage"/>
            <TextField source="radius"/>
            <TextField source="rate"/>
            <TextField source="weaponTypeId"/>
            <TextField source="weaponType.nameRu" label="base name"/>
            <TextField source="damageTypeId"/>
            <TextField source="damageType.name" label="damage type"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const WeaponEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="weaponId" reference="weapons"/>
            <NumberInput disabled source="id"/>
            <TextInput source="characterName" disabled/>
            <TextInput source="weaponType.nameRu" disabled/>
            <TextInput source="damageType.name" disabled/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
            <NumberInput source="damage"/>
            <NumberInput source="radius"/>
            <NumberInput source="rate"/>
            <TextInput source="nameRu" fullWidth/>
            <TextInput source="dscRu" fullWidth/>
        </SimpleForm>
    </Edit>
);


export const WeaponCreate = () => {
    const {data: typesData} = useGetList('weapon_types');
    const {data: damageData} = useGetList('damage_types');
    const [selectedType, setSelectedType] = useState({});
    const [damageType, setSelectedDamageType] = useState({});

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

    const handleDamageTypeChange = (event) => {
        for (const type of damageData) {
            if (type.id === event.target.value) {
                setSelectedDamageType(type);
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
                    source="weaponTypeId"
                    choices={typesData}
                    optionText="nameRu"
                    optionValue="id"
                    label="type"
                    onChange={handleTypeChange}
                />
                <SelectInput
                    source="damageTypeId"
                    choices={damageData}
                    optionText="name"
                    optionValue="id"
                    label="damageType"
                    onChange={handleDamageTypeChange}
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
        setValue('damage', props.type.damage);
        setValue('radius', props.type.radius);
        setValue('rate', props.type.rate);
    }, [setValue, props]);

    return (
        <div>
            <TextInput source="dscRu" fullWidth/>
            <TextInput source="nameRu" fullWidth/>
            <NumberInput source="cost"/>
            <NumberInput source="damage"/>
            <NumberInput source="radius"/>
            <NumberInput source="rate"/>
        </div>
    );
};
