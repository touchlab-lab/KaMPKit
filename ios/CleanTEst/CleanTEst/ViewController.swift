//
//  ViewController.swift
//  CleanTEst
//
//  Created by Kevin Schildhorn on 4/9/20.
//  Copyright © 2020 Touchlab. All rights reserved.
//

import UIKit
import SecondLib
import ThirdLib

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let lib = SecondLib.PlatformKt.addTwo(int: 2)
        let lib2 = ThirdLib.PlatformKt.addTwo(int: 2)

    }


}

