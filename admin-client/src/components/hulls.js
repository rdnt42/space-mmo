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

export const HullList = () => (
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
            <TextField source="hp"/>
            <TextField source="evasion"/>
            <TextField source="armor"/>
            <TextField source="hullTypeId"/>
            <TextField source="hullType.nameRu" label="base name"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const HullEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="hullId" reference="hulls"/>
            <NumberInput disabled source="id"/>
            <TextInput source="characterName" disabled/>
            <TextInput source="hullType.nameRu" disabled/>
            <NumberInput source="hp"/>
            <NumberInput source="evasion"/>
            <NumberInput source="armor"/>
            <TextInput source="nameRu" fullWidth/>
            <TextInput source="dscRu" fullWidth/>
        </SimpleForm>
    </Edit>
);


export const HullCreate = () => {
    const {data: typesData} = useGetList('hull_types');
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
                    source="hullTypeId"
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
        setValue('hp', props.type.hp);
        setValue('evasion', props.type.evasion);
        setValue('armor', props.type.armor);
    }, [setValue, props]);

    return (
        <div>
            <TextInput source="dscRu" fullWidth/>
            <TextInput source="nameRu" fullWidth/>
            <NumberInput source="hp"/>
            <NumberInput source="evasion"/>
            <NumberInput source="armor"/>
        </div>
    );
};
