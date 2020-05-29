//
//  BreedListView.swift
//  KaMPStarteriOS
//
//  Created by Samuel Hill on 5/29/20.
//  Copyright © 2020 Touchlab. All rights reserved.
//

import SwiftUI
import shared

struct BreedListView: View {
    @ObservedObject private var breedList = BreedData()

    var body: some View {
        List(breedList.items, id: \.name){ item in
            Button(action: {
                self.breedList.updateFavorite(item: item)
            }){
                BreedRowView(name:item.name, isFavorite: item.favorite != 0)
            }
        }.alert(isPresented: $breedList.showingError) {
            Alert(title: Text("Error"), message: Text(breedList.error), dismissButton: .default(Text("Dismiss")))
        }
    }
}

class BreedData:ObservableObject {
    @Published var items = [Breed]()
    @Published var error = ""
    @Published var showingError = false
    
    private var model:BreedModel?
    init (){
        model = BreedModel(
            viewUpdate: { summary in
                self.items = summary.allItems
            }, errorUpdate: { errorMessage in
                self.error = errorMessage
                self.showingError = true
            })

        model?.getBreedsFromNetwork()
    }
    
    func updateFavorite(item:Breed){
        model?.updateBreedFavorite(breed: item)
    }
}

struct BreedRowView: View {
    var name:String
    var isFavorite:Bool

    var body: some View {
        HStack {
            Text(name).padding(4)
            Spacer()
            if(isFavorite){
                Image(systemName: "heart.fill").padding(4).foregroundColor(.red)
            }
            else {
                Image(systemName: "heart").padding(4)
            }
        }
    }
}

struct BreedRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group{
            BreedRowView(name:"Beagle", isFavorite: true)
                .previewLayout(.fixed(width: 300, height: 70))
            BreedRowView(name:"Corgi", isFavorite: false)
                .previewLayout(.fixed(width: 300, height: 70))
        }
    }
}
