import {get, put} from '../index';
import * as api from '../../service/api';

export function userList(params) {
    return get(api.api.userList)(params);
}

export function updateUser(params) {
    return put(api.api.updateUser)(params);
}
