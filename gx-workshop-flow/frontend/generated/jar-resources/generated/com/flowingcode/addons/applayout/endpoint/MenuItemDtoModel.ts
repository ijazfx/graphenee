// @ts-nocheck

import MenuItemDto from './MenuItemDto';

import {ObjectModel,StringModel,NumberModel,ArrayModel,BooleanModel,Required,ModelValue,_getPropertyModel} from '@hilla/form';

import {Email,Null,NotNull,NotEmpty,NotBlank,AssertTrue,AssertFalse,Negative,NegativeOrZero,Positive,PositiveOrZero,Size,Past,Future,Digits,Min,Max,Pattern,DecimalMin,DecimalMax} from '@hilla/form';

/**
 * This module is generated from com.flowingcode.addons.applayout.endpoint.MenuItemDto.
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @see {@link file:///var/jenkins_home/workspace/AppLayout-5.x-addon/target/checkout/src/main/java/com/flowingcode/addons/applayout/endpoint/MenuItemDto.java}
 */
export default class MenuItemDtoModel<T extends MenuItemDto = MenuItemDto> extends ObjectModel<T> { 
  declare static createEmptyValue: () => MenuItemDto;

  get label(): StringModel {
    return this[_getPropertyModel]('label', StringModel, [true]);
  }

  get href(): StringModel {
    return this[_getPropertyModel]('href', StringModel, [true]);
  }

  get children(): ArrayModel<ModelValue<MenuItemDtoModel>, MenuItemDtoModel> {
    return this[_getPropertyModel]('children', ArrayModel, [false, MenuItemDtoModel, [false]]);
  }
}
