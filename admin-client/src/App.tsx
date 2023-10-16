import {Admin, Resource} from "react-admin";
import {dataProvider} from "./components/dataProvider";
import {EngineCreate, EngineEdit, EngineList} from "./components/engines";
import {CargoHookCreate, CargoHookEdit, CargoHookList} from "./components/cargoHooks";
import {FuelTankCreate, FuelTankEdit, FuelTankList} from "./components/fuelTanks";
import {WeaponCreate, WeaponEdit, WeaponList} from "./components/weapons";

const App = () => (
    <Admin dataProvider={dataProvider}>
        <Resource name="engines" list={EngineList} edit={EngineEdit} create={EngineCreate}/>
        <Resource name="cargoHooks" list={CargoHookList} edit={CargoHookEdit} create={CargoHookCreate}/>
        <Resource name="fuelTanks" list={FuelTankList} edit={FuelTankEdit} create={FuelTankCreate}/>
        <Resource name="weapons" list={WeaponList} edit={WeaponEdit} create={WeaponCreate}/>
    </Admin>
);

export default App;